package com.chengliang.fsm;

import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Scheduled;
import java.util.Date;

@Component
public class DemoJob {
    @Scheduled(fixedRate = 1000 * 3)
    public void job1(){
        System.out.println("Hello job1: " + new Date());
    }

    @Scheduled(cron = "1/2 * * * * ? *")
    public void job2(){
        System.out.println("Hello job2: " + new Date());
    }
}