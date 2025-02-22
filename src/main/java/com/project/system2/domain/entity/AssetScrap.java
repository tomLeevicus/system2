package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.system2.common.core.utils.EntityUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_scrap")
@Schema(description = "资产报废记录")
public class AssetScrap extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(description = "资产ID", example = "1001")
    private Long assetId;

    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    @Schema(description = "启用时间", example = "2023-01-01")
    private Date startTime;

    @Schema(description = "报废时间", example = "2025-12-31")
    private Date scrapTime;

    @Schema(description = "报废理由", example = "设备老化无法正常使用")
    private String scrapReason;

    // 继承BaseEntity中的字段：
    // createBy, createTime, updateBy, updateTime, delFlag, remark
} 