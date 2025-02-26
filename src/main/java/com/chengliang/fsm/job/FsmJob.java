package com.chengliang.fsm.job;

import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * Fsm定时任务 获取下载中的种子的过期时间
 * 如果种子超过免费时间 则暂停种子的下载
 */
@Component
public class FsmJob {

    /**
     * 注意官方限制: 每分钟最多获取十五个种子的信息
     */
    @Scheduled(fixedRate = 60000)
    public void getDownloadingTorrents() {
        System.out.println("getDownloadingTorrents: " + new Date());
    }

}