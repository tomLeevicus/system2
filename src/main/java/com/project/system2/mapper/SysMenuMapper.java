package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询权限
     */
    List<String> selectMenuPermsByUserId(@Param("userId") Long userId);
} 