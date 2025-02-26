package com.chengliang.fsm;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SolonMain
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args);
    }
}