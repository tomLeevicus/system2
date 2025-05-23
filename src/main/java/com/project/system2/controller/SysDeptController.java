package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.query.SysDeptQuery;
import com.project.system2.domain.dto.AssignUsersToDeptDto;
import com.project.system2.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/system/dept")
@Tag(name = "部门管理", description = "系统部门管理接口")
public class SysDeptController {
    
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @Operation(summary = "获取部门列表", description = "根据条件查询部门列表")
    @Parameter(name = "deptName", description = "部门名称", example = "研发部")
    public Result<List<SysDept>> list(SysDeptQuery query) {
        List<SysDept> depts = deptService.selectDeptList(query);
        return Result.success(depts);
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/treeselect")
    public Result<List<SysDept>> treeselect(SysDeptQuery query) {
        List<SysDept> depts = deptService.selectDeptList(query);
        return Result.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/getInfo/{deptId}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取详细信息")
    @Parameter(name = "deptId", description = "部门ID", example = "200", required = true)
    public Result<SysDept> getInfo(@PathVariable Long deptId) {
        return Result.success(deptService.getDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @PostMapping("/add")
    @Operation(summary = "新增部门", description = "创建新的系统部门")
    @Parameter(name = "dept", description = "部门对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return Result.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return deptService.addDept(dept) ? Result.success() : Result.error();
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @PutMapping("/edit")
    @Operation(summary = "修改部门", description = "更新现有部门信息")
    @Parameter(name = "dept", description = "部门对象", required = true)
    public Result<Void> edit(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if (dept.getParentId().equals(dept.getDeptId())) {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        return deptService.updateDept(dept) ? Result.success() : Result.error();
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @DeleteMapping("/remove/{deptId}")
    @Operation(summary = "删除部门", description = "根据ID删除部门")
    @Parameter(name = "deptId", description = "部门ID", example = "200", required = true)
    public Result<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Result.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.error("部门存在用户,不允许删除");
        }
        return deptService.deleteDept(deptId) ? Result.success() : Result.error();
    }

    /**
     * 分配用户到部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:assign')")
    @PostMapping("/assignUsers")
    @Operation(summary = "分配用户到部门", description = "将指定用户列表分配到指定部门")
    @Parameter(name = "dto", description = "包含部门ID和用户ID列表的对象", required = true)
    public Result<Void> assignUsersToDept(@Validated @RequestBody AssignUsersToDeptDto dto) {
        deptService.assignUsersToDept(dto);
        return Result.success();
    }

    /*@PutMapping("/status")
    @Operation(summary = "修改部门状态", description = "启用或禁用部门")
    @Parameter(name = "dept", description = "包含新状态的部门对象", required = true)
    public Result<Void> changeStatus(@RequestBody SysDept dept) {
        // ...
    }*/
} 