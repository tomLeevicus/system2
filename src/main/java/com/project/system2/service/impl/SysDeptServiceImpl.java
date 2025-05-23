package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.entity.SysUserDept;
import com.project.system2.domain.query.SysDeptQuery;
import com.project.system2.domain.dto.AssignUsersToDeptDto;
import com.project.system2.mapper.SysDeptMapper;
import com.project.system2.mapper.SysRoleMapper;
import com.project.system2.mapper.SysUserDeptMapper;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;

    @Override
    public List<SysDept> selectDeptList(SysDeptQuery query) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<>();
        
        if (query != null) {
            // 根据部门名称模糊查询
            if (StringUtils.isNotBlank(query.getDeptName())) {
                lqw.like(SysDept::getDeptName, query.getDeptName());
            }
            
            // 根据部门状态查询
            if (StringUtils.isNotBlank(query.getStatus())) {
                lqw.eq(SysDept::getStatus, query.getStatus());
            }
            
            // 添加排序条件
            if (StringUtils.isNotBlank(query.getOrderByColumn())) {
                // 这里需要根据实际的排序字段做映射处理
                // 简单示例:
                if ("createTime".equals(query.getOrderByColumn())) {
                    lqw.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), SysDept::getCreateTime);
                }
            } else {
                // 默认排序
                lqw.orderByAsc(SysDept::getParentId)
                   .orderByAsc(SysDept::getOrderNum);
            }
        }
        
        // 查询数据
        List<SysDept> depts = list(lqw);
        
        // 构建树结构
        return buildDeptTree(depts);
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        
        for (SysDept dept : depts) {
            // 如果是顶级节点，遍历该节点的子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        return list.stream().filter(n -> n.getParentId().equals(t.getDeptId())).collect(Collectors.toList());
    }

    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    @Transactional
    public boolean addDept(SysDept dept) {
        return deptMapper.insert(dept) > 0;
    }

    @Override
    @Transactional
    public boolean updateDept(SysDept dept) {
        return deptMapper.updateById(dept) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDept(Long deptId) {
        return deptMapper.deleteById(deptId) > 0;
    }

    @Override
    public SysDept getDeptById(Long deptId) {
        return deptMapper.selectById(deptId);
    }

    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        Long deptId = dept.getDeptId() == null ? -1L : dept.getDeptId();
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .ne(SysDept::getDeptId, deptId);
        return deptMapper.selectCount(wrapper) == 0;
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        return deptMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        LambdaQueryWrapper<SysUserDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserDept::getDeptId, deptId);
        return sysUserDeptMapper.selectCount(wrapper) > 0;
    }

    @Override
    public SysDept getPrimaryDeptByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        // Find one user-department relationship for the user
        SysUserDept userDept = sysUserDeptMapper.selectOne(
            new LambdaQueryWrapper<SysUserDept>()
                .eq(SysUserDept::getUserId, userId)
        );

        if (userDept != null && userDept.getDeptId() != null) {
            // Fetch the department details using the found deptId
            return getDeptById(userDept.getDeptId());
        }
        
        return null; // User is not associated with any department
    }

    @Override
    @Transactional
    public void assignUsersToDept(AssignUsersToDeptDto dto) {
        Long deptId = dto.getDeptId();
        List<Long> userIds = dto.getUserIds();

        if (deptId == null || CollectionUtils.isEmpty(userIds)) {
            // 可以选择抛出异常或直接返回
            return; 
        }

        // 1. 先删除这些指定用户所有的旧部门关联
        LambdaQueryWrapper<SysUserDept> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(SysUserDept::getUserId, userIds);
        sysUserDeptMapper.delete(deleteWrapper);

        // 2. 批量插入新的关联关系 (指定用户 <-> 目标部门)
        List<SysUserDept> userDeptList = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserDept userDept = new SysUserDept();
            userDept.setUserId(userId);
            userDept.setDeptId(deptId);
            // userDept.setPostSort(1); // 设置默认排序，如果需要
            userDeptList.add(userDept);
        }

        // 使用自定义的批量插入方法（如果实现了SQL）或循环插入
        if (!userDeptList.isEmpty()) {
            // 方案一: 如果 SysUserDeptMapper.xml 中实现了 insertBatch
            // sysUserDeptMapper.insertBatch(userDeptList);
            
            // 方案二: 循环单条插入（如果批量插入未实现或数据量不大）
            for (SysUserDept userDept : userDeptList) {
                sysUserDeptMapper.insert(userDept);
            }
        }
    }
} 