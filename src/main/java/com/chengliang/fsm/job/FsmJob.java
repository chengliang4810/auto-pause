package com.chengliang.fsm.job;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.chengliang.fsm.api.FsmApi;
import com.chengliang.fsm.bean.TorrentDetail;
import com.chengliang.fsm.response.FsmResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;

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

    /**
     * 最多获取十五个种子的信息
     */
    @Scheduled(fixedRate = 60000)
    public void getTorrentStatus() {
        FsmResponse<TorrentDetail> fsmResponse = fsmApi.getTorrentDetails("135680");
        if (BooleanUtil.isFalse(fsmResponse.getSuccess())){
            log.error("获取种子详情失败: {}", fsmResponse.getMsg());
            return;
        }
        TorrentDetail torrentDetails = fsmResponse.getData();
        TorrentDetail.Torrent torrent = torrentDetails.getTorrent();
        // 获取种子状态
        String category = torrent.getStatus().getCategory();
        // 获取种子的免费结束时间， 将毫秒值转换为日期格式
        Long endAt = torrent.getStatus().getEndAt();

        if ("free".equals(category)){
            String formatBetween = DateUtil.formatBetween(new Date(), new Date(endAt * 1000), BetweenFormatter.Level.MINUTE);
            log.info("种子详情: 【免费资源】 tid: {} 标题: {}, 文件大小: {}, 免费时间剩余: {}", torrent.getTid(), torrent.getTitle(), torrent.getFileSize(), formatBetween);
        } else {
            log.info("种子详情: 【收费资源】 tid: {} 标题: {}, 文件大小: {}, ", torrent.getTid(), torrent.getTitle(), torrent.getFileSize());
        }
    }

}