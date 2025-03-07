package com.chengliang.fsm.db;

import cn.xbatis.db.annotations.Table;
import cn.xbatis.db.annotations.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;

import java.io.Serializable;

/**
 * 种子进度
 */
@Data
@Table
@Accessors(chain = true)
@AutoTable(comment = "种子进度")
public class TorrentProgress implements Serializable {

    /**
     * 种子信息在系统的唯一标识
     */
    @TableId
    @AutoColumn(comment = "唯一标识")
    private String id;

    /**
     * 种子在平台的唯一标识
     */
    @AutoColumn(comment = "种子平台的唯一标识")
    private String key;
    /**
     * 平台类型
     */
    @AutoColumn(comment = "平台类型")
    private String platform;
    /**
     * 种子的名称
     */
    @AutoColumn(comment = "种子的名称")
    private String name;
    /**
     * 种子的大小
     */
    @AutoColumn(comment = "种子的大小")
    private Long size;
    /**
     * 种子的hash
     */
    @AutoColumn(comment = "种子的hash")
    private String hash;

    /**
     * 到期时间
     */
    @AutoColumn(comment = "到期时间")
    private Long expireTime;

}
