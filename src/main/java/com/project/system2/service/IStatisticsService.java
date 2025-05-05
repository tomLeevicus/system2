package com.project.system2.service;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface IStatisticsService {

    /**
     * 按年统计资产数量。
     *
     * @param year 年份
     * @return 该年份每月资产数量的列表，每个 Map 包含 "month" 和 "count"
     */
    List<Map<String, Object>> getAssetsCountByYearMonthly(int year);

    /**
     * 按月统计资产数量。
     *
     * @param year  年份
     * @param month 月份
     * @return 该月份每日资产数量的列表，每个 Map 包含 "day" 和 "count"
     */
    List<Map<String, Object>> getAssetsCountByMonthDaily(int year, int month);

    /**
     * 按日统计资产数量。
     *
     * @param year  年份
     * @param month 月份
     * @param day   日期
     * @return 当日资产总数
     */
    Long getAssetsCountByDay(int year, int month, int day);

} 