package com.project.system2.service;

public interface ISysUserRoleService {
    
    /**
     * 保存用户和角色关联关系
     */
    boolean saveUserRole(Long userId, Long[] roleIds);
    
    /**
     * 删除用户和角色关联关系
     */
    boolean deleteUserRoleByUserId(Long userId);
    
    /**
     * 批量删除用户和角色关联关系
     */
    boolean deleteUserRole(Long[] userIds);
    
    /**
     * 批量取消授权用户角色
     */
    boolean deleteAuthUsers(Long roleId, Long[] userIds);
    
    /**
     * 批量选择授权用户角色
     */
    boolean insertAuthUsers(Long roleId, Long[] userIds);
    
    /**
     * 检查角色是否有用户在使用
     */
    boolean checkRoleExistUser(Long roleId);

    boolean checkUserIsValidLeader(String starterId, String leaderId);
} 