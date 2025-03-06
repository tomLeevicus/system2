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
@TableName("asset_scrap_record")
@Schema(description = "资产报废审批记录")
public class AssetScrapRecord extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Long id;

    @TableField("scrap_id")
    @Schema(description = "报废记录编号", example = "SCR2023001")
    private Long scrapId;

    @TableField("asset_id")
    @Schema(description = "资产ID", example = "1001")
    private Long assetId;

    @TableField("scrap_user_id")
    @Schema(description = "报废申请人ID", example = "2001")
    private Long scrapUserId;

    @TableField("approver_id")
    @Schema(description = "审批人ID", example = "2002")
    private Long approverId;

    @TableField("approval_comment")
    @Schema(description = "审批意见", example = "设备已过报废年限")
    private String approvalComment;

    @TableField("scrap_time")
    @Schema(description = "操作时间", example = "2023-12-31 10:00:00")
    private Date scrapTime;

    @TableField("is_agreed")
    @Schema(description = "是否同意（0-拒绝，1-同意）", example = "1")
    private Integer isAgreed;

    // 继承字段说明（BaseEntity中已包含）：
    // createBy 创建人
    // createTime 创建时间
    // updateBy 更新人
    // updateTime 更新时间
    // delFlag 删除标志
    // remark 备注
} 