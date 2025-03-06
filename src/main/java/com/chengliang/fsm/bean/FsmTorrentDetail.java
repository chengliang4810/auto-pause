package com.chengliang.fsm.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 种子详情
 */
@Data
public class FsmTorrentDetail implements Serializable {

    /**
     * 种子详情
     */
    private Torrent torrent;

    // 种子信息类
    @Data
    public class Torrent {
        // 种子的唯一标识符
        private Integer tid;
        // 种子的标题
        private String title;
        // 种子的详细内容描述
        private String content;
        // 种子的类型信息
        private Type type;
        // 种子的封面图片链接
        private String cover;
        // 种子文件的原始大小（字节）
        private Long fileRawSize;
        // 种子文件的格式化大小（如 305.43 GB）
        private String fileSize;
        // 种子文件的路径
        private String filePath;
        // 种子文件的哈希值
        private String fileHash;
        // 种子创建的时间戳
        private Long createdTs;
        // 种子的回复数量
        private Integer replyNum;
        // 获取的积分
        private Integer getPoInteger;
        // 完成的次数
        private Integer finish;
        // 是否置顶，N 表示否
        private String isTop;
        // 是否删除，N 表示否
        private String isDel;
        // 种子的标签列表
        private List<String> tags;
        // 女演员信息列表
        private List<String> actress;
        // 截图信息列表
        private List<String> screenshots;
        // 种子的状态信息
        private Status status;
        // 种子的对等节点信息
        private Peers peers;
        // 投票状态
        private String voteStatus;
        // 抢夺信息
        private SnatchInfo snatchInfo;
        // 是否为所有者
        private Boolean isOwner;
        // 是否可以编辑
        private Boolean canEdit;

        // 这里省略 get 和 set 方法
    }

    // 种子类型类
    @Data
    public class Type {
        // 类型的唯一标识符
        private Integer id;
        // 类型的名称
        private String name;

        // 这里省略 get 和 set 方法
    }

    // 种子状态类
    @Data
    public class Status {
        // 状态对应的种子唯一标识符
        private Integer tid;
        // 开始时间戳
        private Long startAt;
        // 结束时间戳
        private Long endAt;
        // 状态名称
        private String name;
        // 状态的类别
        @JSONField(name = "class")
         private String category;
        // 上传系数
        private Integer upCoefficient;
        // 下载系数
        private Integer downCoefficient;

        // 这里省略 get 和 set 方法
    }

    // 种子对等节点类
    @Data
    public class Peers {
        // 上传者数量
        private String upload;
        // 下载者数量
        private String download;

        // 这里省略 get 和 set 方法
    }

    // 种子抢夺信息类
    @Data
    public class SnatchInfo {
        // 抢夺状态
        private String status;
        // 时间戳
        private Long timestamp;

        // 这里省略 get 和 set 方法
    }
}
