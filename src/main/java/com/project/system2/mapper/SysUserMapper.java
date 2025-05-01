package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser selectUserByUserName(@Param("username") String username);

    /**
     * 校验用户名称是否唯一
     */
    int checkUsernameUnique(@Param("username") String username);

    /**
     * 校验手机号码是否唯一
     */
    int checkMobileUnique(@Param("mobile") String mobile);

    /**
     * 校验email是否唯一
     */
    int checkEmailUnique(@Param("email") String email);

    /**
     * 根据角色标识查询用户列表
     * @param roleKey 角色标识
     * @param currentUserId 当前用户ID(用于排除)
     * @return 用户列表
     */
    List<Map<String, Object>> selectUsersByRoleKey(@Param("roleKey") String roleKey,
                                                  @Param("currentUserId") Long currentUserId);
    
    Page<SysUser> selectUserWithDeptRolePage(Page<SysUser> page, @Param("user") SysUser user);

    /**
     * 获取资产审批员列表
     */
    List<SysUser> selectApprovalUsers();

    SysUser getUserInfoHasPassword(@Param("id") Long id);
}