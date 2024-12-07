package com.project.system2.service;

import com.project.system2.domain.entity.SysDept;
import java.util.List;

public interface SysDeptService {
    
    List<SysDept> selectDeptList(SysDept dept);
    
    List<SysDept> buildDeptTree(List<SysDept> depts);
    
    boolean addDept(SysDept dept);
    
    boolean updateDept(SysDept dept);
    
    boolean deleteDept(Long deptId);
    
    SysDept getDeptById(Long deptId);
    
    /**
     * 检查部门名称是否唯一
     */
    boolean checkDeptNameUnique(SysDept dept);
    
    /**
     * 是否存在部门子节点
     */
    boolean hasChildByDeptId(Long deptId);
    
    /**
     * 检查部门是否存在用户
     */
    boolean checkDeptExistUser(Long deptId);
} 