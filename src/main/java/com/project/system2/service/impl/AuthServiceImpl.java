package com.project.system2.service.impl;

import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.exception.ServiceException;
import com.project.system2.common.core.redis.RedisCache;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.AuthService;
import com.project.system2.service.ISysPermissionService;
import com.project.system2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import com.project.system2.common.constant.CacheConstants;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ISysPermissionService permissionService;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public String login(String username, String password, String code,String uuid) {
        // 验证码校验
        if (!validateCaptcha(uuid,code)) {
            throw new BadCredentialsException("验证码错误");
        }
        
        // 1. 验证用户名密码
        SysUser user = userMapper.selectUserByUserName(username);
        
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("密码错误"); 
        }
        
        if ("0".equals(user.getStatus())) {
            throw new ServiceException("用户已停用");
        }
        
        // 2. 获取用户权限
        Set<String> permissions = getMenuPermission(user);
        
        // 3. 创建LoginUser并设置权限
        LoginUser loginUser = new LoginUser(user);
        loginUser.setPermissions(permissions);
        
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


    public boolean validateCaptcha(String uuid, String code) {
        // 参数校验
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(code)) {
            return false;
        }
        
        // 从Redis获取验证码
        String cacheKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String storedCode = redisCache.getCacheObject(cacheKey);
        
        // 删除已使用的验证码（无论对错）
        redisCache.deleteObject(cacheKey);
        
        // 校验验证码（不区分大小写）
        return code.equalsIgnoreCase(storedCode);
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
            roles.addAll(permissionService.selectRolePermissionByUserId(user.getId()));
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
                    if (role.getStatus()==0)
                    {
                        Set<String> rolePerms = permissionService.selectMenuPermsByRoleId(role.getRoleId());
                        role.setPermissions(rolePerms);
                        perms.addAll(rolePerms);
                    }
                }
            }
            else
            {
                perms.addAll(permissionService.selectMenuPermsByUserId(user.getId()));
            }
        }
        return perms;
    }
} 