package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典类型查询对象")
public class SysDictTypeQuery extends BaseQuery {
    
    /**
     * 字典名称
     */
    @Schema(description = "字典名称", example = "用户性别")
    private String dictName;
    
    /**
     * 字典类型
     */
    @Schema(description = "字典类型", example = "sys_user_sex")
    private String dictType;
    
    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "状态（0正常 1停用）", example = "0")
    private String status;
} 