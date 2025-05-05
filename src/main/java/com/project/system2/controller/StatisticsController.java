package com.project.system2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

import com.project.system2.service.IStatisticsService;

/**
 * 统计数据控制器，提供按年、按月、按日统计的功能。
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    // 注入统计服务
    private final IStatisticsService statisticsService;

    // 构造函数注入依赖（推荐方式）
    public StatisticsController(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 按年统计每月资产数量。
     * 例如：/statistics/assets/yearly-monthly?year=2023
     *
     * @param year 要统计的年份
     * @return 按年份统计的每月资产数量
     */
    @PreAuthorize("@ss.hasPermi('statistics:assets:viewYearly')")
    @GetMapping("/assets/yearly-monthly") // 更改路径以更清晰地表示统计内容和粒度
    public ResponseEntity<List<Map<String, Object>>> getAssetsCountByYearMonthly(@RequestParam int year) {
        List<Map<String, Object>> yearlyMonthlyStats = statisticsService.getAssetsCountByYearMonthly(year);
        return ResponseEntity.ok(yearlyMonthlyStats);
    }

    /**
     * 按月统计每日资产数量。
     * 例如：/statistics/assets/monthly-daily?year=2023&month=10
     *
     * @param year  要统计的年份
     * @param month 要统计的月份
     * @return 按月份统计的每日资产数量
     */
    @PreAuthorize("@ss.hasPermi('statistics:assets:viewMonthly')")
    @GetMapping("/assets/monthly-daily") // 更改路径
    public ResponseEntity<List<Map<String, Object>>> getAssetsCountByMonthDaily(@RequestParam int year, @RequestParam int month) {
        List<Map<String, Object>> monthlyDailyStats = statisticsService.getAssetsCountByMonthDaily(year, month);
        return ResponseEntity.ok(monthlyDailyStats);
    }

    /**
     * 按日统计资产总数。
     * 例如：/statistics/assets/daily-total?year=2023&month=10&day=26
     *
     * @param year  要统计的年份
     * @param month 要统计的月份
     * @param day   要统计的日期
     * @return 按日统计的资产总数 (简单返回一个包含 count 的 Map)
     */
    @PreAuthorize("@ss.hasPermi('statistics:assets:viewDaily')")
    @GetMapping("/assets/daily-total") // 更改路径
    public ResponseEntity<Map<String, Long>> getAssetsCountByDay(@RequestParam int year, @RequestParam int month, @RequestParam int day) {
        Long dailyTotal = statisticsService.getAssetsCountByDay(year, month, day);
        // 为了保持 API 响应格式的一致性或提供更清晰的键值对，将其包装在 Map 中
        Map<String, Long> response = Map.of("count", dailyTotal);
        return ResponseEntity.ok(response);
    }

    // 您可以根据需要添加其他统计方法，例如按时间范围统计等。
} 