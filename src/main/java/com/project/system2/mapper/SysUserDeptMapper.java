package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {
    boolean existsUserDept(@Param("userId") Long userId, @Param("deptId") Long deptId);
    
    boolean isSuperior(@Param("subordinateId") Long subordinateId, @Param("leaderId") Long leaderId);

    void updateUserDept(@Param("userId") Long userId, @Param("deptId") Long deptId);

    /**
     * 根据部门ID删除用户部门关联
     * @param deptId 部门ID
     * @return 影响行数
     */
    int deleteByDeptId(@Param("deptId") Long deptId);

    /**
     * 批量插入用户部门关联
     * @param userDeptList 用户部门关联列表
     * @return 影响行数
     */
    int insertBatch(@Param("userDeptList") List<SysUserDept> userDeptList);

    /**
     * 根据用户ID列表删除用户部门关联
     * @param userIds 用户ID列表
     * @return 影响行数
     */
    int deleteByUserIds(@Param("userIds") List<Long> userIds);
    
    /**
     * 根据用户ID查询关联的部门ID
     * @param userId 用户ID
     * @return 部门ID列表
     */
    List<Long> selectDeptIdsByUserId(@Param("userId") Long userId);
} 