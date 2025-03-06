package com.chengliang.fsm.job;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.chengliang.fsm.api.FsmApi;
import com.chengliang.fsm.bean.FsmTorrentDetail;
import com.chengliang.fsm.response.FsmResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final CacheService cacheService;

    /**
     * 剩余API调用次数
     */
    private final static AtomicInteger count = new AtomicInteger(0);

    /**
     * 每分钟重置一次API调用次数
     */
    @Scheduled(fixedRate = 60000)
    public void apiCount(){
        count.set(15);
    }

    /**
     * 获取FSM中我的下载中的列表
     */
    @Scheduled(fixedRate = 60000)
    public void getDownloadingTorrent(){
        if (count.get() <= 0) {
           return;
        }
        FsmResponse<JSONObject> downloadingTorrentResponse = fsmApi.getDownloadingTorrent(1);
        if (BooleanUtil.isFalse(downloadingTorrentResponse.getSuccess())){
            log.error("获取下载中的种子列表失败: {}", downloadingTorrentResponse.getMsg());
            return;
        }

        JSONObject data = downloadingTorrentResponse.getData();
        // 最大页数为1，无需额外处理, 每页30条
        Integer maxPage = data.getInteger("maxPage");
        JSONArray jsonArray = data.getJSONArray("list");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer tid = jsonObject.getInteger("tid");
            String title = jsonObject.getString("title");
            String hash = jsonObject.getString("fileHash");

            log.info("tid: {}, 标题: {}, HASH: {}", tid, title, hash);
        }

        // 减1次
        count.decrementAndGet();
        if (maxPage == 1){
            return;
        }

    }

    /**
     * 最多获取十五个种子的信息
     */
    // @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void getTorrentStatus() {
        FsmResponse<FsmTorrentDetail> fsmResponse = fsmApi.getTorrentDetails("130499");
        if (BooleanUtil.isFalse(fsmResponse.getSuccess())){
            log.error("获取种子详情失败: {}", fsmResponse.getMsg());
            return;
        }
        FsmTorrentDetail fsmTorrentDetails = fsmResponse.getData();
        FsmTorrentDetail.Torrent torrent = fsmTorrentDetails.getTorrent();
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