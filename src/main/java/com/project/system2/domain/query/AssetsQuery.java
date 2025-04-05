package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产查询对象")
public class AssetsQuery extends BaseQuery {

    /**
     * 资产名称
     */
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    /**
     * 资产分类ID
     */
    @Schema(description = "资产分类ID", example = "300")
    private Long assetClassificationId;

    /**
     * 资产型号
     */
    @Schema(description = "资产型号", example = "ThinkPad X1 Carbon Gen 10")
    private String assetModel;

    /**
     * 资产使用状态（0-未启用，1-启用中，2-弃用，3-销毁）
     */
    @Schema(description = "资产使用状态（0-未启用，1-启用中，2-弃用，3-销毁）", example = "1")
    private Integer assetUseStatus;

    /**
     * 资产状态（0-正常，1-删除）
     */
    @Schema(description = "资产状态（0-正常，1-删除）", example = "0")
    private Integer assetStatus;

    /**
     * 使用部门ID
     */
    @Schema(description = "使用部门ID", example = "200")
    private Long assetUseDepartmentId;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1001")
    private Long assetUserId;

    /**
     * 存放位置
     */
    @Schema(description = "存放位置", example = "A栋3楼301室")
    private String assetStorageLocation;
} 