package com.project.system2.service.impl;

import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.exception.ServiceException;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.SysUser;
import com.project.system2.service.AuthService;
import com.project.system2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  TokenService tokenService;


    @Override
    public String login(String username, String password) {
        // 用户验证
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new ServiceException("用户名或密码错误");
        }
        
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
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