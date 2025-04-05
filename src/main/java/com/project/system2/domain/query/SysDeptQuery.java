package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门查询对象")
public class SysDeptQuery extends BaseQuery {

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 部门状态（0正常 1停用）
     */
    @Schema(description = "部门状态（0正常 1停用）")
    private String status;
} 