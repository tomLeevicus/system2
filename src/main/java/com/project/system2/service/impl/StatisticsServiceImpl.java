package com.project.system2.service.impl;

import com.project.system2.mapper.StatisticsMapper;
import com.project.system2.service.IStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    private final StatisticsMapper statisticsMapper;

    // 使用构造函数注入 Mapper
    public StatisticsServiceImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    @Override
    public List<Map<String, Object>> getAssetsCountByYearMonthly(int year) {
        return statisticsMapper.selectAssetsCountByYearMonthly(year);
    }

    @Override
    public List<Map<String, Object>> getAssetsCountByMonthDaily(int year, int month) {
        return statisticsMapper.selectAssetsCountByMonthDaily(year, month);
    }

    @Override
    public Long getAssetsCountByDay(int year, int month, int day) {
        // 如果当天没有数据，Mapper 方法可能返回 null，这里处理一下返回 0L
        Long count = statisticsMapper.selectAssetsCountByDay(year, month, day);
        return count != null ? count : 0L;
    }
} 