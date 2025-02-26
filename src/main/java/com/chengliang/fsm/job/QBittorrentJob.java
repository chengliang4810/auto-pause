package com.chengliang.fsm.job;

import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;
import java.util.Date;

/**
 * qBittorrent 获取下载中状态的种子
 */
@Component
public class QBittorrentJob {

    /**
     * 每3秒执行一次
     * 获取下载中状态的种子
     */
    @Scheduled(fixedRate = 3000)
    public void getDownloadingTorrents() {
        System.out.println("getDownloadingTorrents: " + new Date());
    }

}