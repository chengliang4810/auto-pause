package com.chengliang.fsm.api;


import com.chengliang.fsm.bean.TorrentDetail;
import com.chengliang.fsm.response.FsmResponse;
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
    FsmResponse<TorrentDetail> getTorrentDetails(@Query("tid") String tid);

}
