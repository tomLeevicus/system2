package com.project.system2.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资产折旧计算方式枚举
 */
@Getter
@RequiredArgsConstructor
public enum DepreciationMethodEnum {

    STRAIGHT_LINE(1, "年限平均法"),
    UNITS_OF_PRODUCTION(2, "工作量法"),
    DOUBLE_DECLINING_BALANCE(3, "双倍余额递减法"),
    SUM_OF_THE_YEARS_DIGITS(4, "年数总和法");

    private final Integer code;
    private final String description;

    /**
     * 根据 code 获取枚举
     * @param code 代码
     * @return 对应的枚举，如果找不到返回 null
     */
    public static DepreciationMethodEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DepreciationMethodEnum method : DepreciationMethodEnum.values()) {
            if (method.getCode().equals(code)) {
                return method;
            }
        }
        return null;
    }
} 