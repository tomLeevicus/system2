package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询对象")
public class SysUserQuery extends BaseQuery {

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;


    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Schema(description = "用户性别（0男 1女 2未知）")
    private String gender;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Schema(description = "帐号状态（0正常 1停用）")
    private String status;

    /**
     * 开始创建时间
     */
    @Schema(description = "开始创建时间")
    private String createTimeStart;

    /**
     * 结束创建时间
     */
    @Schema(description = "结束创建时间")
    private String createTimeEnd;
}