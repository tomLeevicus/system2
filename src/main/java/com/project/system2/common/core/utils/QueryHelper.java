package com.project.system2.common.core.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.project.system2.common.constant.Constants;

import java.util.Date;
import java.util.function.Consumer;

/**
 * 查询条件构造工具类
 */
public class QueryHelper {

    /**
     * 设置通用状态条件 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param status 状态值
     * @param statusField 状态字段的Lambda表达式
     * @param <T> 实体类型
     * @param <R> 状态字段类型
     */
    public static <T, R> void setStatus(LambdaQueryWrapper<T> wrapper, R status, SFunction<T, R> statusField) {
        if (status != null) {
            wrapper.eq(statusField, status);
        }
    }

    /**
     * 设置删除标志条件 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param delFlagField 删除标志字段的Lambda表达式
     * @param <T> 实体类型
     */
    public static <T> void setNotDeleted(LambdaQueryWrapper<T> wrapper, SFunction<T, Integer> delFlagField) {
        wrapper.eq(delFlagField, Constants.NOT_DELETED);
    }

    /**
     * 设置模糊查询条件 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param value 查询值
     * @param field 查询字段的Lambda表达式
     * @param <T> 实体类型
     */
    public static <T> void setLike(LambdaQueryWrapper<T> wrapper, String value, SFunction<T, String> field) {
        if (StringUtils.isNotBlank(value)) {
            wrapper.like(field, value);
        }
    }

    /**
     * 设置日期范围查询条件 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param timeField 时间字段的Lambda表达式
     * @param <T> 实体类型
     */
    public static <T> void setTimeRange(LambdaQueryWrapper<T> wrapper, Date beginTime, Date endTime, 
                                        SFunction<T, Date> timeField) {
        if (beginTime != null) {
            wrapper.ge(timeField, beginTime);
        }
        if (endTime != null) {
            wrapper.le(timeField, endTime);
        }
    }

    /**
     * 动态组合查询条件 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param condition 条件是否生效
     * @param consumer 条件消费者
     * @param <T> 实体类型
     */
    public static <T> void condition(LambdaQueryWrapper<T> wrapper, boolean condition, 
                                    Consumer<LambdaQueryWrapper<T>> consumer) {
        if (condition) {
            consumer.accept(wrapper);
        }
    }

    /**
     * 设置排序 - Lambda方式
     *
     * @param wrapper 查询条件构造器
     * @param isAsc 是否升序
     * @param fields 排序字段的Lambda表达式
     * @param <T> 实体类型
     */
    @SafeVarargs
    public static <T> void setOrderBy(LambdaQueryWrapper<T> wrapper, boolean isAsc, SFunction<T, ?>... fields) {
        if (fields != null && fields.length > 0) {
            if (isAsc) {
                // 单独处理每个字段
                for (SFunction<T, ?> field : fields) {
                    wrapper.orderByAsc(field);
                }
            } else {
                // 单独处理每个字段
                for (SFunction<T, ?> field : fields) {
                    wrapper.orderByDesc(field);
                }
            }
        }
    }
    
    /**
     * 设置分页查询的默认排序（如果未指定排序）
     *
     * @param wrapper 查询条件构造器
     * @param defaultField 默认排序字段的Lambda表达式
     * @param <T> 实体类型
     */
    public static <T> void setDefaultOrder(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> defaultField) {
        // 检查wrapper中是否已经有排序字段
        // 注：MyBatis-Plus目前不直接提供检查排序字段的方法，此处仅为示例
        // 实际使用时可能需要根据具体情况调整
        wrapper.orderByDesc(defaultField);
    }
} 