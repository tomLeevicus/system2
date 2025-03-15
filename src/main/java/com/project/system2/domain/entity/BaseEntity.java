package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，提供通用字段
 */
@Data
@Schema(description = "基础实体类")
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @Schema(description = "创建者ID", example = "1001")
    @TableField("create_by")
    private Long createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    @TableField("create_time")
    private Date createTime;

    /** 更新者 */
    @Schema(description = "最后更新者ID", example = "1002")
    @TableField("update_by")
    private Long updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后更新时间", example = "2023-01-15 14:30:00")
    @TableField("update_time")
    private Date updateTime;

    /** 备注 */
    @Schema(description = "备注信息", example = "系统初始化数据")
    @TableField("remark")
    private String remark;

    /** 删除标志 */
    @TableField("del_flag")
    @Schema(description = "删除标志（0存在 1删除）", example = "0", defaultValue = "0")
    private Integer delFlag;
} 