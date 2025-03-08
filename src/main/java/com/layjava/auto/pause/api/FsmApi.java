package com.layjava.auto.pause.api;


import com.alibaba.fastjson2.JSONObject;
import com.layjava.auto.pause.bean.FsmTorrentDetail;
import com.layjava.auto.pause.response.FsmResponse;
import com.dtflys.forest.annotation.*;

/**
 * FSM API接口
 */
@ForestClient
@BaseRequest(baseURL = "https://fsm.name/api", headers = {
        "APITOKEN : ${APITOKEN}",
})
public interface FsmApi {

    /**
     * 获取种子详情
     * @param tid 种子id
     * @return 种子详情
     */
    @Get("/Torrents/details?page=1")
    FsmResponse<FsmTorrentDetail> getTorrentDetails(@Query("tid") String tid);

    /**
     * 获取我的下载中的种子
     * @param page
     * @return
     */
    @Get("/Torrents/listMyDownload?page=${page}")
    FsmResponse<JSONObject> getDownloadingTorrent(@Query("page") Integer page);

}
