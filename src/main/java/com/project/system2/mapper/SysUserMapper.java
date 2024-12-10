package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser selectUserByUsername(@Param("username") String userName);

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
} 