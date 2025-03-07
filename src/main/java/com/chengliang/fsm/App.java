package com.chengliang.fsm;

import cn.xbatis.core.XbatisConfig;
import com.chengliang.fsm.mapper.CommonMapper;
import org.dromara.autotable.solon.annotation.EnableAutoTable;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;

@SolonMain
@EnableAutoTable
@EnableScheduling
public class App {

    public static void main(String[] args) {
        Solon.start(App.class, args);
        XbatisConfig.setSingleMapperClass(CommonMapper.class);
    }

}