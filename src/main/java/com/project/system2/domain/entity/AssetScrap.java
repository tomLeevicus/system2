package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_scrap")
@Schema(description = "资产报废主记录")
public class AssetScrap extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Long id;

    @TableField("asset_id")
    @Schema(description = "资产ID", example = "1001")
    private Long assetId;

    @TableField("asset_name")
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    @TableField("start_time")
    @Schema(description = "启用时间", example = "2023-01-01")
    private Date startTime;

    @TableField("scrap_time")
    @Schema(description = "报废时间", example = "2023-12-31")
    private Date scrapTime;

    @TableField("scrap_reason")
    @Schema(description = "报废原因", example = "设备老化无法正常使用")
    private String scrapReason;

    // 继承字段说明（BaseEntity中已包含）：
    // createBy 创建人
    // createTime 创建时间
    // updateBy 更新人
    // updateTime 更新时间
    // delFlag 删除标志
    // remark 备注
} 