package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "基础实体")
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @TableField("create_by")
    @Schema(description = "创建人", example = "admin")
    private Long createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    @Schema(description = "创建时间", example = "2023-01-01 10:00:00")
    private Date createTime;

    /** 更新者 */
    @TableField("update_by")
    @Schema(description = "更新人", example = "admin")
    private Long updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    @Schema(description = "更新时间", example = "2023-01-02 14:30:00")
    private Date updateTime;

    @TableField("del_flag")
    @Schema(description = "逻辑删除标志（0-存在，1-删除）", example = "0")
    private int delFlag;

    @TableField("remark")
    @Schema(description = "备注", example = "系统自动创建")
    private String remark;
} 