package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dept")
public class SysDeptController {
    
    @Autowired
    private SysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public Result<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.success(depts);
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/treeselect")
    public Result<List<SysDept>> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.success(deptService.buildDeptTree(depts));
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public Result<SysDept> getInfo(@PathVariable Long deptId) {
        return Result.success(deptService.getDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @PostMapping
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
    @PutMapping
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
    @DeleteMapping("/{deptId}")
    public Result<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Result.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.error("部门存在用户,不允许删除");
        }
        return deptService.deleteDept(deptId) ? Result.success() : Result.error();
    }
} 