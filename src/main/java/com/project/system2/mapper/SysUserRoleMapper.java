package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    boolean hasLeaderRole(@Param("userId") String userId);
    boolean isSuperior(@Param("subordinateId") String subordinateId, 
                      @Param("leaderId") String leaderId);
} 