package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.exception.ServiceException;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysRole;
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
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

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
        SysUser user = userMapper.selectUserByUserName(username);
        
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("密码错误"); 
        }
        
        if ("1".equals(user.getStatus())) {
            throw new ServiceException("用户已停用");
        }
        
        // 3. 创建LoginUser
        LoginUser loginUser = new LoginUser(user);
        
        // 4. 生成token
        return tokenService.createToken(loginUser);
    }

    @Override
    public Map<String, Object> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        
        // 角色权限列表
        Set<String> roles = getRolePermission(user);
        Set<String> permissions = getMenuPermission(user);
        
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

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    private Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(permissionService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    private Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles))
            {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles)
                {
                    if (StringUtils.equals(role.getStatus(), "0"))
                    {
                        Set<String> rolePerms = permissionService.selectMenuPermsByRoleId(role.getRoleId());
                        role.setPermissions(rolePerms);
                        perms.addAll(rolePerms);
                    }
                }
            }
            else
            {
                perms.addAll(permissionService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
} 