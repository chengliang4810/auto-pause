package com.chengliang.fsm.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.xbatis.core.sql.executor.chain.QueryChain;
import com.alibaba.fastjson2.JSONObject;
import com.chengliang.fsm.api.FsmApi;
import com.chengliang.fsm.api.QBittorrentApi;
import com.chengliang.fsm.bean.FsmTorrentDetail;
import com.chengliang.fsm.db.TorrentProgress;
import com.chengliang.fsm.mapper.CommonMapper;
import com.chengliang.fsm.response.FsmResponse;
import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fsm定时任务 获取下载中的种子的过期时间
 * 如果种子超过免费时间 则暂停种子的下载
 * !!! 注意: 官方限制: 每分钟最多十五个请求
 */
@Slf4j
@Component
@AllArgsConstructor
public class FsmJob {

    private final FsmApi fsmApi;
    private final CommonMapper mapper;
    private final QBittorrentApi qBittorrentApi;

    /**
     * 获取数据库中没有过期时间的种子
     * 并获取种子的过期时间
     * 如果种子超过免费时间 则暂停种子的下载
     */
    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void getExpireTorrent() {
        // 查询数据库下载中状态的超过免费期的种子
        List<TorrentProgress> torrentProgressList = QueryChain.of(mapper, TorrentProgress.class)
                // 下载中
                .eq(TorrentProgress::getDownloading, true)
                // 过期时间大于等于当前时间(减一分钟时间)的种子。 提前一分钟是为了防止下载时间超过了免费时间，导致计算下载流量问题
                .lte(TorrentProgress::getExpireTime,  DateUtil.offsetMinute(new Date(), -1).getTime())
                // 按照时间升序排序获取15个种子
                .orderBy(TorrentProgress::getExpireTime)
                .list();

        if (CollUtil.isEmpty(torrentProgressList)) {
            return;
        }

        // 获取种子的hash值
        List<String> hashList = torrentProgressList.stream().map(TorrentProgress::getHash).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        String hashes = StrUtil.join("|", hashList);

        ForestConfiguration config = Forest.config();
        Object qbcookie = config.getVariableValue("qbcookie");
        // 暂停种子的下载
        ForestResponse<JSONObject> fsmResponse = qBittorrentApi.pauseTorrent(hashes, qbcookie);
        if (!fsmResponse.statusOk()) {
            log.error("暂停种子下载失败: {}", fsmResponse.getResult());
            return;
        }

        List<TorrentProgress> collect = torrentProgressList.stream().map(torrentProgress -> new TorrentProgress().setId(torrentProgress.getId()).setDownloading(false)).collect(Collectors.toList());
        mapper.update(collect);
        log.info("暂停种子成功");
    }

    /**
     * 获取FSM种子的免费日期信息
     */
    @Scheduled(fixedRate = 6000, initialDelay = 5000)
    public void getFsmTorrentInfo() {
        // 查询数据库中没有过期时间的种子最多15个
        List<TorrentProgress> torrentProgressList = QueryChain.of(mapper, TorrentProgress.class)
                .limit(15)
                .isNull(TorrentProgress::getExpireTime)
                .list();

        if (CollUtil.isEmpty(torrentProgressList)) {
            return;
        }
        torrentProgressList.forEach(torrentProgress -> {
            FsmResponse<FsmTorrentDetail> fsmResponse = fsmApi.getTorrentDetails(torrentProgress.getKey());
            if (BooleanUtil.isFalse(fsmResponse.getSuccess())) {
                log.error("获取种子详情失败: {}", fsmResponse.getMsg());
                return;
            }
            FsmTorrentDetail fsmTorrentDetails = fsmResponse.getData();
            FsmTorrentDetail.Torrent torrent = fsmTorrentDetails.getTorrent();
            // 获取种子状态
            String category = torrent.getStatus().getCategory();
            // 获取种子的免费结束时间， 将毫秒值转换为日期格式
            Long endAt = torrent.getStatus().getEndAt();
            if (endAt == null) {
                // 等于空值，说明种子已经过期
                mapper.update(new TorrentProgress().setId(torrentProgress.getId()).setExpireTime(System.currentTimeMillis()));
                log.info("种子详情: 【付费资源】 tid: {} 标题: {}, 文件大小: {}, ", torrent.getTid(), torrent.getTitle(), torrent.getFileSize());
                return;
            }
            // 更新数据库中的种子信息过期时间
            mapper.update(new TorrentProgress().setId(torrentProgress.getId()).setExpireTime(endAt * 1000));

            if ("free".equals(category)) {
                String formatBetween = DateUtil.formatBetween(new Date(), new Date(endAt * 1000), BetweenFormatter.Level.MINUTE);
                log.info("种子详情: 【免费资源】 tid: {} 标题: {}, 文件大小: {}, 免费时间剩余: {}", torrent.getTid(), torrent.getTitle(), torrent.getFileSize(), formatBetween);
            } else {
                log.info("种子详情: 【付费资源】 tid: {} 标题: {}, 文件大小: {}, ", torrent.getTid(), torrent.getTitle(), torrent.getFileSize());
            }
        });


    }

}