package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
@Schema(description = "字典类型实体")
public class SysDictType extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "字典主键", example = "1")
    private Long dictId;

    @Schema(description = "字典名称", example = "用户性别")
    private String dictName;

    @Schema(description = "字典类型", example = "sys_user_sex")
    private String dictType;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private String status;

} 