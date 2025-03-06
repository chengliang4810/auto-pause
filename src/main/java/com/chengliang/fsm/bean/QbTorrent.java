package com.chengliang.fsm.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * BT种子详细信息实体类
 */
@Data
public class QbTorrent implements Serializable {
    /**
     * 添加时间（Unix时间戳）
     */
    @JSONField(name = "added_on")
    private Long addedOn;

    /**
     * 剩余下载量（字节）
     */
    @JSONField(name = "amount_left")
    private Long amountLeft;

    /**
     * 是否启用自动种子管理
     */
    @JSONField(name = "auto_tmm")
    private Boolean autoTmm;

    /**
     * 文件分块可用率（0.0-1.0）
     */
    @JSONField(name = "availability")
    private Double availability;

    /**
     * 分类名称
     */
    @JSONField(name = "category")
    private String category;

    /**
     * 已完成数据量（字节）
     */
    @JSONField(name = "completed")
    private Long completed;

    /**
     * 完成时间（Unix时间戳）
     */
    @JSONField(name = "completion_on")
    private Long completionOn;

    /**
     * 内容存储绝对路径
     */
    @JSONField(name = "content_path")
    private String contentPath;

    /**
     * 下载限速（字节/秒，-1表示无限制）
     */
    @JSONField(name = "dl_limit")
    private Long dlLimit;

    /**
     * 当前下载速度（字节/秒）
     */
    @JSONField(name = "dlspeed")
    private Long dlSpeed;

    /**
     * 累计下载量（字节）
     */
    @JSONField(name = "downloaded")
    private Long downloaded;

    /**
     * 本次会话下载量（字节）
     */
    @JSONField(name = "downloaded_session")
    private Long downloadedSession;

    /**
     * 预估剩余时间（秒）
     */
    @JSONField(name = "eta")
    private Long eta;

    /**
     * 是否优先下载首尾分块
     */
    @JSONField(name = "f_l_piece_prio")
    private Boolean flPiecePrio;

    /**
     * 是否强制开始
     */
    @JSONField(name = "force_start")
    private Boolean forceStart;

    /**
     * Torrent哈希值
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 是否为私有种子（v5.0.0+）
     */
    @JSONField(name = "isPrivate")
    private Boolean isPrivate;

    /**
     * 最后活动时间（Unix时间戳）
     */
    @JSONField(name = "last_activity")
    private Long lastActivity;

    /**
     * Magnet链接
     */
    @JSONField(name = "magnet_uri")
    private String magnetUri;

    /**
     * 最大分享率限制
     */
    @JSONField(name = "max_ratio")
    private Double maxRatio;

    /**
     * 最大做种时间（秒）
     */
    @JSONField(name = "max_seeding_time")
    private Long maxSeedingTime;

    /**
     * 种子名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 完整Peer数量（做种者）
     */
    @JSONField(name = "num_complete")
    private Integer numComplete;

    /**
     * 不完整Peer数量（下载者）
     */
    @JSONField(name = "num_incomplete")
    private Integer numIncomplete;

    /**
     * 当前连接中的下载者数量
     */
    @JSONField(name = "num_leechs")
    private Integer numLeechs;

    /**
     * 当前连接中的做种者数量
     */
    @JSONField(name = "num_seeds")
    private Integer numSeeds;

    /**
     * 优先级（-1表示禁用队列或种子模式）
     */
    @JSONField(name = "priority")
    private Integer priority;

    /**
     * 下载进度（0.0-1.0）
     */
    @JSONField(name = "progress")
    private Double progress;

    /**
     * 当前分享率（最大9999）
     */
    @JSONField(name = "ratio")
    private Double ratio;

    /**
     * 分享率限制（与max_ratio的区别待确认）
     */
    @JSONField(name = "ratio_limit")
    private Double ratioLimit;

    /**
     * 存储路径
     */
    @JSONField(name = "save_path")
    private String savePath;

    /**
     * 已做种时间（秒）
     */
    @JSONField(name = "seeding_time")
    private Long seedingTime;

    /**
     * 做种时间限制（秒）
     * -2：自动种子管理启用
     * -1：无限制
     * ≥0：具体限制值
     */
    @JSONField(name = "seeding_time_limit")
    private Long seedingTimeLimit;

    /**
     * 最后完整可见时间（Unix时间戳）
     */
    @JSONField(name = "seen_complete")
    private Long seenComplete;

    /**
     * 是否启用顺序下载
     */
    @JSONField(name = "seq_dl")
    private Boolean seqDl;

    /**
     * 已选择文件的总大小（字节）
     */
    @JSONField(name = "size")
    private Long size;

    /**
     * 当前状态（下载中/做种中/暂停等）
     */
    @JSONField(name = "state")
    private String state;

    /**
     * 是否启用超级种子模式
     */
    @JSONField(name = "super_seeding")
    private Boolean superSeeding;

    /**
     * 标签列表（逗号分隔）
     */
    @JSONField(name = "tags")
    private String tags;

    /**
     * 总活动时间（秒）
     */
    @JSONField(name = "time_active")
    private Long timeActive;

    /**
     * 全部文件总大小（字节，含未选文件）
     */
    @JSONField(name = "total_size")
    private Long totalSize;

    /**
     * 当前有效Tracker地址
     */
    @JSONField(name = "tracker")
    private String tracker;

    /**
     * 上传限速（字节/秒，-1表示无限制）
     */
    @JSONField(name = "up_limit")
    private Long upLimit;

    /**
     * 累计上传量（字节）
     */
    @JSONField(name = "uploaded")
    private Long uploaded;

    /**
     * 本次会话上传量（字节）
     */
    @JSONField(name = "uploaded_session")
    private Long uploadedSession;

    /**
     * 当前上传速度（字节/秒）
     */
    @JSONField(name = "upspeed")
    private Long upSpeed;
}
