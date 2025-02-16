package com.project.system2.service;

import java.util.Map;

public interface AuthService {
    
    String login(String username, String password,String code,String uuid);
    
    Map<String, Object> getInfo();
    
    void logout();
} 