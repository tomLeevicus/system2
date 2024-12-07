package com.project.system2.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @ExcelProperty("字典编码")
    private Long dictCode;

    @ExcelProperty("字典排序")
    private Long dictSort;

    @ExcelProperty("字典标签")
    private String dictLabel;

    @ExcelProperty("字典键值")
    private String dictValue;

    @ExcelProperty("字典类型")
    private String dictType;

    @ExcelProperty("样式属性")
    private String cssClass;

    @ExcelProperty("表格样式")
    private String listClass;

    @ExcelProperty("是否默认")
    private String isDefault;

    @ExcelProperty("状态")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private String delFlag;
} 