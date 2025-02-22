package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysUser;
import java.util.List;

public interface ISysUserService {
    
    SysUser getUserByUsername(String username);
    
    boolean addUser(SysUser user);
    
    boolean updateUser(SysUser user);
    
    boolean deleteUser(Long userId);
    
    SysUser getUserById(Long userId);
    
    /**
     * 修改用户角色关联关系
     */
    boolean updateUserRole(Long userId, Long[] roleIds);


    /**
     * 批量删除用户
     */
    boolean deleteUserByIds(Long[] userIds);

    /**
     * 重置用户密码
     */
    boolean resetPwd(SysUser user);

    /**
     * 分页查询用户列表
     */
    Page<SysUser> selectUserPage(Page<SysUser> page, SysUser user);
} 