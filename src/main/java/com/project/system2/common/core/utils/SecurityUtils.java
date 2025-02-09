package com.project.system2.common.core.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import com.project.system2.common.core.domain.model.LoginUser;

public class SecurityUtils {
    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户角色列表
     */
    public static Set<String> getRoles() {
        try {
            Collection<? extends GrantedAuthority> authorities = getAuthentication().getAuthorities();
            Set<String> roles = new HashSet<>();
            if (!CollectionUtils.isEmpty(authorities)) {
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority().startsWith("ROLE_")) {
                        roles.add(authority.getAuthority().substring(5));
                    }
                }
            }
            return roles;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 获取用户权限列表
     */
    public static Set<String> getPermissions() {
        try {
            Collection<? extends GrantedAuthority> authorities = getAuthentication().getAuthorities();
            Set<String> perms = new HashSet<>();
            if (!CollectionUtils.isEmpty(authorities)) {
                for (GrantedAuthority authority : authorities) {
                    if (!authority.getAuthority().startsWith("ROLE_")) {
                        perms.add(authority.getAuthority());
                    }
                }
            }
            return perms;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
} 