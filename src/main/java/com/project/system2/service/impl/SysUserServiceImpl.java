package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.query.SysUserQuery;
import com.project.system2.domain.dto.UserDTO;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.ISysUserRoleService;
import com.project.system2.service.ISysUserService;
import com.project.system2.service.ISysRoleService;
import com.project.system2.service.ISysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysUserRoleService userRoleService;
    
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public boolean addUser(SysUser user) {
//        初始化密码为 123456
        user.setPassword(passwordEncoder.encode("123456"));
        int result = userMapper.insert(user);
        if (result > 0 && user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
             userRoleService.saveUserRole(user.getId(), user.getRoleIds());
        }
        return result > 0;
    }

    @Override
    @Transactional
    public boolean updateUser(SysUser user) {
        boolean isUpdated = userMapper.updateById(user) > 0;

        if (user.getRoleIds() != null) {
            userRoleService.saveUserRole(user.getId(), user.getRoleIds());
        }

        if (user.getDept() != null && user.getDept().getDeptId() != null) {
             userRoleService.updateUserDept(user.getId(), user.getDept().getDeptId()); 
        }

        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public SysUser getUserById(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user != null) {
            List<SysRole> roles = roleService.selectRolesByUserId(userId);
            user.setRoles(roles);

            SysDept dept = deptService.getPrimaryDeptByUserId(userId);
            user.setDept(dept);
            user.setDeptId(dept != null ? dept.getDeptId() : null);
            user.setDeptName(dept != null ? dept.getDeptName() : null);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean updateUserRole(Long userId, List<Long> roleIds) {
        return userRoleService.saveUserRole(userId, roleIds);
    }

    @Override
    @Transactional
    public boolean deleteUserByIds(Long[] userIds) {
        userRoleService.deleteUserRole(userIds);
        return Arrays.stream(userIds)
                .map(this::deleteUser)
                .reduce(true, (a, b) -> a && b);
    }

    @Override
    @Transactional
    public boolean resetPwd(SysUser user) {
        user.setPassword(passwordEncoder.encode("123456"));
        return updateUser(user);
    }

    @Override
    public Page<SysUser> selectUserPage(Page<SysUser> page, SysUserQuery query) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        
        lqw.ne(SysUser::getId, "1");
        
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId != null) {
            lqw.ne(SysUser::getId, currentUserId);
        }
        
        if (query != null) {
            if (query.getDeptId() != null) {
                lqw.eq(SysUser::getDeptId, query.getDeptId());
            }
            
            if (StringUtils.isNotBlank(query.getNickname())) {
                lqw.like(SysUser::getNickname, query.getNickname());
            }
            
            if (StringUtils.isNotBlank(query.getMobile())) {
                lqw.like(SysUser::getMobile, query.getMobile());
            }
            
            if (StringUtils.isNotBlank(query.getGender())) {
                lqw.eq(SysUser::getGender, query.getGender());
            }
            
            if (StringUtils.isNotBlank(query.getStatus())) {
                lqw.eq(SysUser::getStatus, query.getStatus());
            }
            
            if (StringUtils.isNotBlank(query.getCreateTimeStart())) {
                lqw.ge(SysUser::getCreateTime, query.getCreateTimeStart());
            }
            
            if (StringUtils.isNotBlank(query.getCreateTimeEnd())) {
                lqw.le(SysUser::getCreateTime, query.getCreateTimeEnd());
            }
            
            if (StringUtils.isNotBlank(query.getOrderByColumn())) {
                if ("createTime".equals(query.getOrderByColumn())) {
                    lqw.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), SysUser::getCreateTime);
                }
            } else {
                lqw.orderByDesc(SysUser::getCreateTime);
            }
        }
        
        Page<SysUser> userPage = page(page, lqw);
        
        List<SysUser> userList = userPage.getRecords();
        
        if (!userList.isEmpty()) {
            for (SysUser user : userList) {
                List<SysRole> roles = roleService.selectRolesByUserId(user.getId());
                user.setRoles(roles);
            }
        }
        
        return userPage;
    }

    @Override
    public List<SysUser> getApprovalUsers() {
        return userMapper.selectApprovalUsers();
    }

    @Override
    @Transactional
    public boolean changeUserPassword(UserDTO userDto) {
        if (userDto == null || userDto.getId() == null || StringUtils.isBlank(userDto.getOldPassword()) || StringUtils.isBlank(userDto.getNewPassword())) {
            log.error("修改密码请求参数不完整: {}", userDto);
            return false; // 或者抛出 IllegalArgumentException
        }

        // 1. 根据ID查询用户
        SysUser currentUser = userMapper.getUserInfoHasPassword(userDto.getId());
        if (currentUser == null) {
            log.warn("尝试修改不存在用户的密码, 用户ID: {}", userDto.getId());
            // 可以考虑抛出异常 new ServiceException("用户不存在");
            return false;
        }

        String oldPassword = passwordEncoder.encode(userDto.getOldPassword());

        // 2. 校验旧密码
        if (!passwordEncoder.matches(userDto.getOldPassword(), currentUser.getPassword())) {
            log.warn("用户ID: {} 的旧密码不匹配", userDto.getId());
             // 可以考虑抛出异常 new ServiceException("旧密码不正确");
            return false;
        }

        // 3. 验证新密码复杂度 (可选, 根据需要添加)
        // if (!isPasswordComplex(userDto.getNewPassword())) {
        //     throw new ServiceException("新密码不符合复杂度要求");
        // }

        // 4. 加密新密码
        String encodedNewPassword = passwordEncoder.encode(userDto.getNewPassword());

        // 5. 更新用户密码
        SysUser userToUpdate = new SysUser();
        userToUpdate.setId(userDto.getId());
        userToUpdate.setPassword(encodedNewPassword);
        // 如果您的 BaseEntity 自动处理更新时间和更新人，则不需要手动设置
        // userToUpdate.setUpdateTime(new Date());
        // userToUpdate.setUpdateBy(SecurityUtils.getUsername()); 

        int updateCount = userMapper.updateById(userToUpdate);
        if (updateCount <= 0) {
            log.error("更新用户ID: {} 的密码失败", userDto.getId());
            // 可以考虑抛出异常 new ServiceException("更新密码时发生错误");
        }

        return updateCount > 0;
    }
} 