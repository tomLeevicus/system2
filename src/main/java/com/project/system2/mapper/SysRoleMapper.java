package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.system2.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);

    @Select("SELECT role_id, role_name, role_key, role_sort, status, remark, "
        + "create_by, create_time, update_by, update_time, del_flag "
        + "FROM sys_role "
        + "${ew.customSqlSegment}") // 使用MyBatis-Plus的Wrapper条件
    IPage<SysRole> selectRolePage(IPage<SysRole> page, @Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);
} 