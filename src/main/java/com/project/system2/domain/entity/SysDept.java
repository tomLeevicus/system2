package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@Schema(description = "部门实体")
public class SysDept extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "部门ID", example = "200")
    private Long deptId;

    @Schema(description = "父部门ID", example = "0")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    @Schema(description = "部门名称", example = "研发部")
    private String deptName;

    @Schema(description = "显示顺序", example = "1")
    private Integer orderNum;

    @Schema(description = "负责人", example = "张三")
    private String leader;

    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Schema(description = "邮箱", example = "devdept@example.com")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）", example = "0")
    private String status;

    @TableField(exist = false)
    @Schema(description = "子部门列表")
    private List<SysDept> children = new ArrayList<>();

    public boolean isTop() {
        return parentId == null || parentId == 0L;
    }
} 