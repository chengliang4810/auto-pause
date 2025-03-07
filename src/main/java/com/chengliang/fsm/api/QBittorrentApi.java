package com.chengliang.fsm.api;

import com.alibaba.fastjson2.JSONObject;
import com.chengliang.fsm.bean.QbTorrent;
import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;

import java.util.List;

@ForestClient
@BaseRequest(baseURL = "${QBHOST}/api/v2", headers = {
        "Referer : ${QBHOST}",
})
public interface QBittorrentApi {

    @Post(value = "/auth/login", data = "username=${QBUSERNAME}&password=${QBPASSWORD}")
    ForestResponse<String> authLogin();

    /**
     * 获取种子列表
     * @param filter 过滤条件
     * @return 种子列表
     */
    @Get(value = "/torrents/info", headers = "cookie: ${qbcookie}")
    ForestResponse<List<QbTorrent>> getTorrentsInfoList(@Query("filter") String filter, @Var("qbcookie") Object cookie);

    /**
     * 获取种子属性
     * @param hash hash值
     * @param cookie 登录信息
     */
    @Get(value = "/torrents/properties", headers = "cookie: ${qbcookie}")
    ForestResponse<JSONObject> getTorrentProperties(@Query("hash") String hash, @Var("qbcookie") Object cookie);


    /**
     * 获取种子属性
     * @param hashes hash值 多个 | 分割
     * @param cookie 登录信息
     */
    @Post(value = "/torrents/pause", headers = "cookie: ${qbcookie}")
    ForestResponse<JSONObject> pauseTorrent(@FormBody("hashes") String hashes, @Var("qbcookie") Object cookie);
}
