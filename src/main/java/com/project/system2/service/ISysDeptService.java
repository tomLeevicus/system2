package com.project.system2.service;

import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.query.SysDeptQuery;
import com.project.system2.domain.dto.AssignUsersToDeptDto;
import java.util.List;

public interface ISysDeptService {
    
    List<SysDept> selectDeptList(SysDeptQuery query);
    
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
    
    /**
     * 根据用户ID获取用户的主部门
     * @param userId 用户ID
     * @return 主部门信息，如果未分配则返回null
     */
    SysDept getPrimaryDeptByUserId(Long userId);
    
    /**
     * 分配用户到指定部门
     * @param dto 包含部门ID和用户ID列表的DTO
     */
    void assignUsersToDept(AssignUsersToDeptDto dto);
} 