package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产入库
 */
@Data
@TableName(value = "asset_storage")
@Schema(description = "资产入库实体")
public class AssetStorage extends BaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "入库记录ID", example = "400")
    private Long id;

    /**
     * 资产信息id (可能不再需要，因为信息现在直接存在入库记录中)
     * 如果下游逻辑不再需要通过它查询模板，可以考虑移除
     */
    /*@TableField(value = "asset_id")
    @Schema(description = "关联资产模板ID (可能弃用)", example = "1001")
    private Long assetId;*/

    /**
     * 资产名称
     */
    @TableField(value = "asset_name")
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    /**
     * 资产分类id (新增)
     */
    @TableField(value = "asset_classification_id")
    @Schema(description = "资产分类ID", example = "300")
    private Long assetClassificationId;

    /**
     * 资产型号 (新增)
     */
    @TableField(value = "asset_model")
    @Schema(description = "资产型号", example = "ThinkPad X1")
    private String assetModel;

    /**
     * 资产价格-数字 (新增)
     */
    @TableField(value = "asset_price_num")
    @Schema(description = "资产单价", example = "15000")
    private BigDecimal assetPriceNum;

    /**
     * 资产价格-单位id (新增)
     */
    @TableField(value = "asset_price_unit")
    @Schema(description = "价格单位ID", example = "1")
    private Long assetPriceUnit;

    /**
     * 资产存放地 (新增)
     */
    @TableField(value = "asset_storage_location")
    @Schema(description = "存放位置", example = "A栋3楼仓库")
    private String assetStorageLocation;

    /**
     * 资产负责人id (新增)
     */
    @TableField(value = "asset_user_id")
    @Schema(description = "负责人ID", example = "1001")
    private Long assetUserId;

    /**
     * 资产使用部门id (新增)
     */
    @TableField(value = "asset_use_department_id")
    @Schema(description = "使用部门ID", example = "200")
    private Long assetUseDepartmentId;

    /**
     * 入库数量
     */
    @TableField(value = "inbound_quantity")
    @Schema(description = "入库数量", example = "50")
    private Integer inboundQuantity;

    /**
     * 操作者名称
     */
    @TableField(value = "operator_name")
    @Schema(description = "操作人姓名", example = "张三")
    private String operatorName;

    /**
     * 入库时间
     */
    @TableField(value = "warehouse_time")
    @Schema(description = "入库时间", example = "2023-03-15 14:30:00")
    private Date warehouseTime;

    /*
    @Schema(description = "最低库存预警值", example = "10")
    private Integer minStock;

    @Schema(description = "库存位置", example = "A区-3号仓库")
    private String location;
    */
}