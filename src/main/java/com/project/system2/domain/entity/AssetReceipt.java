package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

/**
 * 资产领用
 */
@Data
@TableName(value = "asset_receipt")
@Schema(description = "资产领用实体")
public class AssetReceipt extends BaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "领用记录ID", example = "600")
    private Long id;

    /**
     * 资产信息id
     */
    @TableField(value = "asset_id")
    @Schema(description = "关联资产ID", example = "1001")
    private Long assetId;

    /**
     * 资产名称'
     */
    @TableField(value = "asset_name")
    private String assetName;

    /**
     * 领用人名称
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    /**
     * 领用日期
     */
    @TableField(value = "collection_date")
    @Schema(description = "领用日期", example = "2023-04-01")
    private Date collectionDate;

    /**
     * 领用说明
     */
    @TableField(value = "instructions_for_use")
    @Schema(description = "领用说明", example = "")
    private String instructionsForUse;

    /**
     * 是否长期领用 1：是  0： 否
     */
    @TableField(value = "is_long_term_use")
    private Integer isLongTermUse;

    /**
     * 归还日期
     */
    @TableField(value = "return_time")
    private Date returnTime;

    /**
     * 归还状态 0：未规还 1：已归还
     */
    @TableField(value = "return_status")
    private Integer returnStatus;

    /**
     * 审核状态 0：未审核 1：审核通过 2：审核未通过
     */
    @TableField(value = "review_status")
    private Integer reviewStatus;
/*
    *//**
     * 验收结果（0-不合格，1-合格）
     *//*
    @TableField(value = "result")
    @Schema(description = "验收结果（0-不合格，1-合格）", example = "1")
    private Integer result;

    *//**
     * 验收说明
     *//*
    @TableField(value = "remark")
    @Schema(description = "验收说明", example = "设备功能正常，外观无损伤")
    private String remark;

    @Schema(description = "验收单号", example = "REC_2023001")
    private String receiptNo;
    
    @Schema(description = "验收结论", example = "符合采购要求")
    private String conclusion;
    
    @Schema(description = "验收附件URL", example = "/attachments/2023/rec001.pdf")
    private String attachment;
    
    @Schema(description = "验收状态（0-待验收，1-已验收）", example = "1")
    private Integer status;*/
}