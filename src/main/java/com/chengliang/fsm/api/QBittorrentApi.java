package com.chengliang.fsm.api;

import com.chengliang.fsm.bean.QbTorrent;
import com.dtflys.forest.annotation.*;
import com.dtflys.forest.callback.OnLoadCookie;
import com.dtflys.forest.callback.OnSaveCookie;
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
}
