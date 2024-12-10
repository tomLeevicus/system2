package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.exception.ServiceException;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.AuthService;
import com.project.system2.service.SysPermissionService;
import com.project.system2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(String username, String password) {
        // 1. 验证用户名密码
        SysUser user = userMapper.selectUserByUsername(username);
        
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("密码错误"); 
        }
        
        if ("1".equals(user.getStatus())) {
            throw new ServiceException("用户已停用");
        }

        // 2. 获取用户权限
        Set<String> permissions = permissionService.getMenuPermission(user);
        
        // 3. 创建LoginUser
        LoginUser loginUser = new LoginUser(user, permissions);
        
        // 4. 生成token
        return tokenService.createToken(loginUser);
    }

    @Override
    public Map<String, Object> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        
        // 角色权限列表
        Set<String> roles = SecurityUtils.getRoles();
        Set<String> permissions = SecurityUtils.getPermissions();
        
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("roles", roles);
        data.put("permissions", permissions);
        return data;
    }

    @Override
    public void logout() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            tokenService.deleteToken(loginUser.getToken());
        }
    }
} 