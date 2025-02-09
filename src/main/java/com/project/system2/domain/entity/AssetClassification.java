package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.util.Date;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产分类表
 */
@Data
@TableName(value = "asset_classification")
@Schema(description = "资产分类实体")
public class AssetClassification extends BaseEntity {
    /**
     * 资产分类id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "分类ID", example = "300")
    private Long id;

    /**
     * 序号
     */
    @TableField(value = "sort_num")
    private Integer sortNum;

    /**
     * 分类名称
     */
    @TableField(value = "category_name")
    @Schema(description = "分类名称", example = "电子设备")
    private String categoryName;

    // 非数据库字段，用于统计资产数量
    @TableField(exist = false)
    private Integer assetCount;

    /*@Schema(description = "分类编码", example = "ELEC_DEVICE")
    private String code;

    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    @Schema(description = "分类层级", example = "1")
    private Integer level;*/
}