package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.dto.UserDTO;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.query.SysUserQuery;

import java.util.ArrayList;
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
    boolean updateUserRole(Long userId, List<Long> roleIds);


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
    Page<SysUser> selectUserPage(Page<SysUser> page, SysUserQuery query);

    /**
     * 获取资产审批员列表
     */
    List<SysUser> getApprovalUsers();

    /**
     * 用户修改密码
     * @param userDto 包含用户ID、旧密码和新密码的对象
     * @return 是否成功
     */
    boolean changeUserPassword(UserDTO userDto);
} 