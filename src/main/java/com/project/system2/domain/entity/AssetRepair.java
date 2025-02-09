package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName(value = "asset_repair")
@Schema(description = "资产维修记录实体")
public class AssetRepair {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "维修记录ID", example = "500")
    private Long id;

    /**
     * 资产信息id
     */
    @TableField(value = "asset_id")
    @Schema(description = "关联资产ID", example = "1001")
    private Long assetId;

    /**
     * 资产名称
     */
    @TableField(value = "asset_name")
    private String assetName;

    /**
     * 维修原因
     */
    @TableField(value = "reason_for_repair")
    private String reasonForRepair;

    /**
     * 报修日期
     */
    @TableField(value = "repair_date")
    @Schema(description = "维修日期", example = "2023-03-15")
    private LocalDate repairDate;

    /**
     * 维修费用（元）
     */
    @TableField(value = "cost")
    @Schema(description = "维修费用（元）", example = "1500.00")
    private BigDecimal cost;

    /**
     * 维修状态 0：待审核 1：审核完成 2：审核未通过
     */
    @TableField(value = "`status`")
    @Schema(description = "维修状态（0-进行中，1-已完成）", example = "1")
    private Integer status;

    @TableField(value = "create_by")
    private Long createBy;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_by")
    private Long updateBy;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "del_flag")
    @Schema(description = "删除标志（0-存在，1-删除）", example = "0")
    private Integer delFlag;

/*    @Schema(description = "维修单号", example = "REP_2023001")
    private String repairNo;

    @Schema(description = "故障描述", example = "屏幕显示异常")
    private String faultDescription;

    @Schema(description = "维修费用（元）", example = "500.00")
    private BigDecimal repairCost;

    @Schema(description = "维修结果（0-未修复，1-已修复）", example = "1")
    private Integer repairResult;*/
}