package com.project.system2.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Schema(description = "字典数据实体")
public class SysDictData extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @ExcelProperty("字典编码")
    @Schema(description = "字典编码", example = "100")
    private Long dictCode;

    @ExcelProperty("字典排序")
    private Long dictSort;

    @ExcelProperty("字典标签")
    @Schema(description = "字典标签", example = "正常状态")
    private String dictLabel;

    @ExcelProperty("字典键值")
    @Schema(description = "字典键值", example = "0")
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
    @Schema(description = "状态（0正常 1停用）", example = "0")
    private String status;

} 