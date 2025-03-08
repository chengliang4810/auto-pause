package com.layjava.auto.pause.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Fsm响应结果
 */
@Data
@Accessors(chain = true)
public class FsmResponse<T> implements Serializable {
    /**
     * 请求结果
     */
    private Boolean success;

    /**
     * 描述信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;
}
