package com.project.system2.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.entity.Assets;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class PersonalAssetDTO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "领用记录ID", example = "600")
    private Long id;

    /**
     * 领用日期
     */
    @TableField(value = "collection_date")
    @Schema(description = "领用日期", example = "2023-04-01")
    private Date collectionDate;

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
    /**
     * 资产信息id
     */
    @TableField(value = "asset_id")
    @Schema(description = "关联资产ID", example = "1001")
    private Long assetId;

    /**
     * 资产编号
     */
    @Schema(description = "资产唯一编号", example = "ASSET-2023-001")
    private String assetNumber;

    /**
     * 资产名称
     */
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    /**
     * 资产存放地
     */
    @Schema(description = "存放位置", example = "A栋3楼301室")
    private String assetStorageLocation;
} 