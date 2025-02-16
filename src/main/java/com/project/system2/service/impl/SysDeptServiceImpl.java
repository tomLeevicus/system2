package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.mapper.SysDeptMapper;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dept.getDeptName()),SysDept::getDeptName,dept.getDeptName())
                .eq(StringUtils.isNotEmpty(dept.getStatus()),SysDept::getStatus,dept.getStatus());
        // 可以添加更多的查询条件
        List<SysDept> sysDepts = deptMapper.selectList(wrapper);
        return buildDeptTree(sysDepts);
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
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeptId, deptId);
        return userMapper.selectCount(wrapper) > 0;
    }
} 