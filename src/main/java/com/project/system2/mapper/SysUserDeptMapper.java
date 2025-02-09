package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {
    boolean existsUserDept(@Param("userId") Long userId, @Param("deptId") Long deptId);
    
    boolean isSuperior(@Param("subordinateId") Long subordinateId, @Param("leaderId") Long leaderId);
} 