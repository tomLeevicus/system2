package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产入库
 */
@Data
@TableName(value = "asset_storage")
@Schema(description = "资产库存实体")
public class AssetStorage extends BaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "库存ID", example = "STOCK_001")
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
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

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
    @TableField(value = "`warehouse time`")
    @Schema(description = "入库时间", example = "2023-03-15 14:30:00")
    private Date warehouseTime;

    @TableField(value = "create_by")
    private Long createBy;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_by")
    private Long updateBy;

    @TableField(value = "update_time")
    private Date updateTime;

/*    @Schema(description = "最低库存预警值", example = "10")
    private Integer minStock;

    @Schema(description = "库存位置", example = "A区-3号仓库")
    private String location;*/
}