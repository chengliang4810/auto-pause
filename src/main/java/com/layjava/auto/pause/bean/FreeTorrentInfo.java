package com.layjava.auto.pause.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class FreeTorrentInfo implements Serializable {

    /**
     * 平台种子唯一标识
     */
    private String id;

    /**
     * 文件Hash值
     */
    private String hash;

    /**
     * 标题
     */
    private String title;

    /**
     * 免费截止时间
     */
    private Date freeDeadline;
}
