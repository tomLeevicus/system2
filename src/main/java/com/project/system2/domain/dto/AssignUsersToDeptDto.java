package com.project.system2.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Schema(description = "分配用户到部门请求 DTO")
public class AssignUsersToDeptDto {

    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID", required = true, example = "101")
    private Long deptId;

    @NotEmpty(message = "用户ID列表不能为空")
    @Schema(description = "要分配的用户ID列表", required = true, example = "[1001, 1002]")
    private List<Long> userIds;

    // 可以根据需要添加其他字段，例如 postSort 的默认值或单独指定
} 