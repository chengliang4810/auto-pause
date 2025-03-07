package com.chengliang.fsm.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.chengliang.fsm.api.QBittorrentApi;
import com.chengliang.fsm.bean.QbTorrent;
import com.chengliang.fsm.db.TorrentProgress;
import com.chengliang.fsm.mapper.CommonMapper;
import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * qBittorrent 获取下载中状态的种子
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QBittorrentJob {

    private final QBittorrentApi qBittorrentApi;
    private final CommonMapper mapper;
    /**
     * 下载中的Fsm种子
     * key为文件fsm tid
     * value为种子信息
     */
    public static final Map<String, QbTorrent> fsmQbTorrentMap = new ConcurrentHashMap<>();

    /**
     * 登录qbittorrent获取cookie
     */
    public void loginQb() {
        String qbHost = System.getenv("QB_HOST");
        String qbUsername = System.getenv("QB_USERNAME");
        String qbPassword = System.getenv("QB_PASSWORD");
        log.info("QBittorrent连接信息, 主机 {}, 账号: {}, 密码: {}", qbHost, qbUsername, qbPassword);

        ForestResponse<String> forestResponse = qBittorrentApi.authLogin();

        if (forestResponse.statusIs(403)){
            log.warn("登录失败, 服务器响应：{} 。 \"如果出现内容为 \" 身份认证失败次数过多，您的 IP 地址已被封禁。 \", 请重启QBittorrent服务\"", forestResponse.getResult());
            return;
        }

        String cookie = Optional.ofNullable(forestResponse.getHeaders()
                        .getValue("Set-Cookie"))
                .map(str -> StrUtil.split(str, ";"))
                .map(list -> list.get(0))
                .orElse("");
        if (StrUtil.isBlank(cookie)){
            log.warn("QBittorrent登录失败, 请仔细检查账号、密码、服务器地址等信息是否正确");
            return;
        }

        // 设置全局变量 TODO forest在solon环境暂时存在问题，无法使用下方的方式
        Forest.config().setVariableValue("qbcookie", (method) -> cookie);
    }

    /**
     * 每3秒执行一次
     * 获取下载中状态的种子
     */
    @Scheduled(fixedRate = 3000)
    public void getDownloadingTorrents() {

        // 是否需要登录
        if (!Forest.config().isVariableDefined("qbcookie")) {
            loginQb();
        }

        ForestConfiguration config = Forest.config();
        Object qbcookie = config.getVariableValue("qbcookie");

        // 下载中的种子
        ForestResponse<List<QbTorrent>> forestResponse = qBittorrentApi.getTorrentsInfoList("downloading", qbcookie);
        if (forestResponse.statusIsNot(200) ) {
            log.warn("获取种子列表失败, 响应码: {} , 响应内容: {}", forestResponse.statusCode(), forestResponse.getResult());
            return;
        }

        List<QbTorrent> qbTorrentList = forestResponse.getResult();
        if (CollUtil.isEmpty(qbTorrentList)){
            return;
        }

        List<QbTorrent> fsmQbList = qbTorrentList.stream()
                .filter(qb -> qb.getTracker().contains("https://connects.icu/Announce") || qb.getTracker().contains("https://nextpt.net/Announce"))
                .filter(qb -> "downloading".equals(qb.getState()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(fsmQbList)){
            return;
        }
        // hash: 14fb282f456bb855de8593eac40d46ba0fb42a80
        fsmQbList.forEach(qbTorrent -> {
            // downloading  , pausedDL
            log.info("下载列表的下载状态的FSM种子： {} - {},", qbTorrent.getName(), qbTorrent.getState());
        });

        fsmQbList.forEach(qbTorrent -> {
            String hash = qbTorrent.getHash();
            ForestResponse<JSONObject> torrentPropertiesResponse = qBittorrentApi.getTorrentProperties(hash, qbcookie);
            // 种子的注释， FSM在备注中存储了tid值
            String comment = torrentPropertiesResponse.getResult().getString("comment");
            String tid = parseCommentGetTid(comment);

            if (StrUtil.isBlank(tid)){
                return;
            }

            TorrentProgress torrentProgress = new TorrentProgress();
            torrentProgress.setId("fsm" + tid);
            torrentProgress.setDownloading(true);

            TorrentProgress progress = mapper.getById(TorrentProgress.class, "fsm" + tid);
            if (progress != null){
                mapper.update(torrentProgress);
                return;
            }

            torrentProgress.setHash(hash);
            torrentProgress.setKey(tid);
            torrentProgress.setName(qbTorrent.getName());
            torrentProgress.setPlatform("fsm");
            torrentProgress.setSize(qbTorrent.getTotalSize());
            torrentProgress.setHash(hash);
            mapper.save(torrentProgress);

        });
    }

    /**
     * 解析comment获取tid
     * @param comment
     * @return
     */
    private String parseCommentGetTid(String comment) {
        if (StrUtil.startWith(comment, "https://fsm.name/Torrents/details?tid=")) {
            String tid = comment.replace("https://fsm.name/Torrents/details?tid=", "");
            return StrUtil.trim(tid);
        }
        return null;
    }


}