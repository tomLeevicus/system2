package com.project.system2.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产信息表
 */
@Data
@Schema(description = "资产详细信息实体")
public class Assets extends BaseEntity {
    /**
    * uuid
    */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "资产唯一标识", example = "ASSET_2023001")
    private Long id;

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
    * 资产分类id
    */
    @Schema(description = "关联分类ID", example = "300")
    private Long assetClassificationId;

    /**
    * 资产型号
    */
    @Schema(description = "设备型号", example = "ThinkPad X1 Carbon Gen 10")
    private String assetModel;

    /**
    * 资产购买时间
    */
    @Schema(description = "采购时间", example = "2023-03-15")
    private Date assetPurchaseTime;

    /**
    * 资产启用时间
    */
    @Schema(description = "启用时间", example = "2023-04-01")
    private Date assetUseTime;

    /**
    * 资产负责人id
    */
    @Schema(description = "负责人ID", example = "1001")
    private Long assetUserId;

    /**
    * 资产存放地
    */
    @Schema(description = "存放位置", example = "A栋3楼301室")
    private String assetStorageLocation;

    /**
    * 资产使用部门id
    */
    @Schema(description = "使用部门ID", example = "200")
    private Long assetUseDepartmentId;

    /**
    * 资产价格-数字
    */
    @Schema(description = "采购价格数值", example = "15000")
    private Integer assetPriceNum;

    /**
    * 资产价格-单位id
    */
    @Schema(description = "价格单位ID", example = "1")
    private Long assetPriceUnit;

    /**
    * 资产使用状态 0:未启用 1:启用中 2:弃用 3:销毁
    */
    @Schema(description = "使用状态（0-未启用，1-启用中，2-弃用，3-销毁）", example = "1")
    private Integer assetUseStatus;

    /**
    * 状态 0：正常 1：删除
    */
    @Schema(description = "数据状态（0-正常，1-删除）", example = "0")
    private Integer assetStatus;

    /*@Schema(description = "资产分类路径", example = "电子设备/笔记本电脑")
    private String categoryPath;

    @Schema(description = "使用年限（年）", example = "5")
    private Integer serviceLife;

    @Schema(description = "折旧率（百分比）", example = "20.00")
    private BigDecimal depreciationRate;

    @Schema(description = "维保状态（0-在保，1-过保）", example = "0")
    private Integer maintenanceStatus;*/
}