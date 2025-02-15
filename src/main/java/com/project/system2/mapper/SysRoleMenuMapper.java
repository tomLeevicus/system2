package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 保存角色菜单关系
     */
    boolean saveRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Long[] menuIds);
} 