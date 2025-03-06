package com.chengliang.fsm.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.chengliang.fsm.api.QBittorrentApi;
import com.chengliang.fsm.bean.QbTorrent;
import com.dtflys.forest.Forest;
import com.dtflys.forest.callback.OnLoadCookie;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.*;
import com.dtflys.forest.solon.SolonForestVariableValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;
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
    /**
     * 下载中的Fsm种子
     * key为文件Hash
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
    //@Scheduled(fixedRate = 1000)
    public void getDownloadingTorrents() {

        if (!Forest.config().isVariableDefined("qbcookie")) {
            loginQb();
            return;
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

        List<QbTorrent> fsmQbList = qbTorrentList.stream().filter(qb -> qb.getTracker().contains("https://connects.icu/Announce") || qb.getTracker().contains("https://nextpt.net/Announce")).collect(Collectors.toList());
        if (CollUtil.isEmpty(fsmQbList)){
            return;
        }
        // hash: 14fb282f456bb855de8593eac40d46ba0fb42a80
        log.info("下载中的FSM种子： {}", fsmQbList);
    }


}