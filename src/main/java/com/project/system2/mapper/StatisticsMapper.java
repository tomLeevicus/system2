package com.project.system2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 统计数据 Mapper 接口
 */
@Mapper
public interface StatisticsMapper {

    /**
     * 按年份查询每月资产数量
     *
     * @param year 年份
     * @return 每月资产数量列表 (Map: month, count)
     */
    List<Map<String, Object>> selectAssetsCountByYearMonthly(@Param("year") int year);

    /**
     * 按年月查询每日资产数量
     *
     * @param year  年份
     * @param month 月份
     * @return 每日资产数量列表 (Map: day, count)
     */
    List<Map<String, Object>> selectAssetsCountByMonthDaily(@Param("year") int year, @Param("month") int month);

    /**
     * 按年月日查询当日资产数量
     *
     * @param year  年份
     * @param month 月份
     * @param day   日期
     * @return 当日资产总数
     */
    Long selectAssetsCountByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

} 