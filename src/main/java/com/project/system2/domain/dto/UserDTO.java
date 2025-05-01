package com.project.system2.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class UserDTO {
    private Long id;

    private String oldPassword;

    private String newPassword;

}
