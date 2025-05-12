package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.query.AssetsQuery;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Arrays;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.project.system2.domain.enums.DepreciationMethodEnum;
import com.project.system2.utils.DepreciationCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;

@Service
@Slf4j
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets> implements IAssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public Result<Assets> getAssetById(Long id) {
        Assets asset = assetsMapper.selectById(id);
        return Result.success(asset);
    }

    @Override
    public Result<Boolean> addAsset(Assets asset) {
        int rows = assetsMapper.insert(asset);
        return Result.success(rows > 0);
    }

    @Override
    public Result<Boolean> updateAsset(Assets asset) {
        int rows = assetsMapper.updateById(asset);
        return Result.success(rows > 0);
    }

    @Override
    public Result<Boolean> deleteAsset(Long id) {
        int rows = assetsMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    public Result<IPage<Assets>> queryAssetsList(AssetsQuery query) {
        Page<Assets> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<Assets> wrapper = new LambdaQueryWrapper<>();
        
        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(Assets::getAssetName, query.getAssetName());
        }
        
        // 添加资产分类ID查询条件
        if (query.getAssetClassificationId() != null) {
            wrapper.eq(Assets::getAssetClassificationId, query.getAssetClassificationId());
        }
        
        // 添加资产型号查询条件
        if (StringUtils.isNotBlank(query.getAssetModel())) {
            wrapper.like(Assets::getAssetModel, query.getAssetModel());
        }
        
        // 添加资产使用状态查询条件
        if (query.getAssetUseStatus() != null) {
            wrapper.eq(Assets::getAssetUseStatus, query.getAssetUseStatus());
        }
        
        // 添加资产状态查询条件
        if (query.getAssetStatus() != null) {
            wrapper.eq(Assets::getAssetStatus, query.getAssetStatus());
        }
        
        // 添加使用部门ID查询条件
        if (query.getAssetUseDepartmentId() != null) {
            wrapper.eq(Assets::getAssetUseDepartmentId, query.getAssetUseDepartmentId());
        }
        
        // 添加负责人ID查询条件
        if (query.getAssetUserId() != null) {
            wrapper.eq(Assets::getAssetUserId, query.getAssetUserId());
        }
        
        // 添加存放位置查询条件
        if (StringUtils.isNotBlank(query.getAssetStorageLocation())) {
            wrapper.like(Assets::getAssetStorageLocation, query.getAssetStorageLocation());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("createTime".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), Assets::getCreateTime);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(Assets::getCreateTime);
        }
        
        IPage<Assets> pageResult = page(page, wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Map<String, Long> getAssetStatisticsByStatus() {
        // Define all possible statuses
        List<String> allStatuses = Arrays.asList("闲置", "在用", "维修", "报废", "未知"); 
        // Initialize the result map with all statuses and count 0
        Map<String, Long> resultMap = new HashMap<>();
        for (String status : allStatuses) {
            resultMap.put(status, 0L);
        }

        // Query assets and get actual counts
        List<Assets> allAssets = assetsMapper.selectList(
            new LambdaQueryWrapper<Assets>().select(Assets::getAssetUseStatus)
        );

        // Group by status and count existing assets
        Map<String, Long> actualCounts = allAssets.stream()
                .collect(Collectors.groupingBy(
                    asset -> mapStatusToString(asset.getAssetUseStatus()), 
                    Collectors.counting()
                ));

        // Merge actual counts into the result map
        resultMap.putAll(actualCounts);

        return resultMap;
    }

    // Helper method to convert status code to String (replace with actual logic)
    private String mapStatusToString(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "闲置"; // Example mapping
            case 1: return "在用"; // Example mapping
            case 2: return "维修"; // Example mapping
            case 3: return "报废"; // Example mapping
            // Add other status codes as needed
            default: return "状态[" + status + "]";
        }
    }

    /**
     * 批量保存资产信息
     * 利用 ServiceImpl 的 saveBatch 方法实现
     * @param assetsList 资产列表
     * @return 是否成功
     */
    @Override
    @Transactional // 建议在实现类方法上也加上事务注解，虽然调用方已有
    public boolean saveBatchAssets(List<Assets> assetsList) {
        return saveBatch(assetsList); // 调用 ServiceImpl 的 saveBatch 方法
    }

    /**
     * 批量计算并更新资产折旧
     * @param assetIds 资产ID列表
     * @return 包含更新结果和错误信息的Map
     */
    @Override
    @Transactional
    public Result<Map<String, Object>> calculateAndApplyDepreciation(List<Long> assetIds) {
        List<String> errors = new ArrayList<>();
        List<Assets> updatedAssets = new ArrayList<>();

        for (Long id : assetIds) {
            Assets asset = this.getById(id); // Use ServiceImpl's getById
            if (asset == null) {
                errors.add("未找到ID为 " + id + " 的资产");
                continue;
            }

            if (asset.getDepreciationMethod() == null) {
                errors.add("资产 " + asset.getAssetNumber() + " 未设置折旧方法");
                continue;
            }

            if (asset.getAssetUseTime() == null) {
                errors.add("资产 " + asset.getAssetNumber() + " 未设置启用时间");
                continue;
            }

             if (asset.getAssetPriceNum() == null) {
                errors.add("资产 " + asset.getAssetNumber() + " 未设置原值");
                continue;
            }

             if (asset.getSalvageValue() == null) {
                errors.add("资产 " + asset.getAssetNumber() + " 未设置预计净残值");
                continue;
            }

             if (asset.getServiceLife() == null || asset.getServiceLife() <= 0) {
                 // 工作量法可能不需要预计年限，但其他方法需要
                 if (!DepreciationMethodEnum.UNITS_OF_PRODUCTION.getCode().equals(asset.getDepreciationMethod())) {
                    errors.add("资产 " + asset.getAssetNumber() + " 未设置有效的预计使用年限");
                    continue;
                 }
            }

            // 初始化累计折旧（如果为 null）
            if (asset.getAccumulatedDepreciation() == null) {
                asset.setAccumulatedDepreciation(BigDecimal.ZERO);
            }

            // 检查是否已完全折旧
            BigDecimal depreciableBase = asset.getAssetPriceNum().subtract(asset.getSalvageValue());
            if (asset.getAccumulatedDepreciation().compareTo(depreciableBase) >= 0) {
                 // 已达到或超过残值，不再折旧
                 // 可以选择记录日志或添加到某种状态报告中
                 continue;
            }

            BigDecimal annualDepreciation = BigDecimal.ZERO;
            LocalDate useDate = asset.getAssetUseTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            // 计算当前是使用的第几年，考虑不足一年的情况可能需要按月或天折旧，此处简化为按年计算
            long yearsInUse = ChronoUnit.YEARS.between(useDate, currentDate);
            int currentDepreciationYear = (int) yearsInUse + 1; // 当前是使用的第几年

            // 确保计算的年份不超过预计年限 (工作量法除外)
            if (asset.getServiceLife() != null && currentDepreciationYear > asset.getServiceLife()
                 && !DepreciationMethodEnum.UNITS_OF_PRODUCTION.getCode().equals(asset.getDepreciationMethod())){
                 // 已经超过预计使用年限，不再计算折旧（除非是特殊调整）
                 // 此处简单跳过，也可以根据会计准则进行最后调整
                 continue;
            }

            try {
                DepreciationMethodEnum method = DepreciationMethodEnum.getByCode(asset.getDepreciationMethod());
                if (method == null) {
                    errors.add("资产 " + asset.getAssetNumber() + " 的折旧方法代码无效: " + asset.getDepreciationMethod());
                    continue;
                }

                BigDecimal originalCost = asset.getAssetPriceNum();
                BigDecimal salvageValue = asset.getSalvageValue();
                Integer usefulLife = asset.getServiceLife(); // 可能为 null
                BigDecimal beginningBookValue = originalCost.subtract(asset.getAccumulatedDepreciation());

                switch (method) {
                    case STRAIGHT_LINE:
                         if (usefulLife == null || usefulLife <= 0) {
                            errors.add("资产 " + asset.getAssetNumber() + " 使用年限平均法，但缺少有效年限");
                            continue;
                         }
                        annualDepreciation = DepreciationCalculator.calculateStraightLineDepreciation(originalCost, salvageValue, usefulLife);
                        break;
                    case UNITS_OF_PRODUCTION:
                        // 如前所述，无法计算，因为缺少年实际工作量
                        errors.add("资产 " + asset.getAssetNumber() + " 使用工作量法，但系统不支持自动计算（缺少年实际工作量）");
                        continue; // 跳过此资产的计算
                    case DOUBLE_DECLINING_BALANCE:
                         if (usefulLife == null || usefulLife <= 0) {
                            errors.add("资产 " + asset.getAssetNumber() + " 使用双倍余额递减法，但缺少有效年限");
                            continue;
                         }
                         // 在计算双倍余额递减法时，需要考虑是否已接近或达到残值，并进行最后调整。
                         // DepreciationCalculator 中的方法已包含这一逻辑，但依赖于传入正确的beginningBookValue和salvageValue。
                         // 确保当前计算不会使净值低于残值。
                        annualDepreciation = DepreciationCalculator.calculateDoubleDecliningBalanceDepreciation(beginningBookValue, salvageValue, usefulLife, currentDepreciationYear);
                        break;
                    case SUM_OF_THE_YEARS_DIGITS:
                        if (usefulLife == null || usefulLife <= 0) {
                            errors.add("资产 " + asset.getAssetNumber() + " 使用年数总和法，但缺少有效年限");
                            continue;
                         }
                         // 在计算年数总和法时，需要确保累计折旧不超过可折旧总额。
                         // DepreciationCalculator 中的方法计算的是当年的折旧额，调用方需要管理累计折旧。
                        annualDepreciation = DepreciationCalculator.calculateSumOfTheYearsDigitsDepreciation(originalCost, salvageValue, usefulLife, currentDepreciationYear);
                        break;
                    default:
                        errors.add("资产 " + asset.getAssetNumber() + " 存在未知的折旧方法");
                        continue;
                }

                // 更新累计折旧和现有估值
                BigDecimal newAccumulated = asset.getAccumulatedDepreciation().add(annualDepreciation);

                // 确保累计折旧不超过可折旧总额 (原值 - 残值)
                 if (newAccumulated.compareTo(depreciableBase) > 0) {
                    newAccumulated = depreciableBase;
                     // 重新计算实际增加的折旧额，避免多加
                    annualDepreciation = newAccumulated.subtract(asset.getAccumulatedDepreciation());
                 }

                // 如果计算出的年折旧额为负数（可能发生在最后一年调整），则置为0
                if (annualDepreciation.compareTo(BigDecimal.ZERO) < 0) {
                    annualDepreciation = BigDecimal.ZERO;
                    // 累计折旧可能需要回退到折旧前的状态，但这取决于业务规则。
                    // 这里简单保持累计折旧不变，因为 annualDepreciation 已经被置为0
                    // newAccumulated = asset.getAccumulatedDepreciation(); // 累计折旧不变
                }

                asset.setAccumulatedDepreciation(newAccumulated.setScale(2, RoundingMode.HALF_UP));
                asset.setEstimatedValue(originalCost.subtract(newAccumulated).setScale(2, RoundingMode.HALF_UP));
                asset.setDepreciationCalculationTime(new Date()); // 更新计算时间

                updatedAssets.add(asset);

            } catch (IllegalArgumentException e) {
                errors.add("计算资产 " + asset.getAssetNumber() + " 折旧时出错: " + e.getMessage());
                log.error("计算资产 {} 折旧时参数无效: {}", asset.getAssetNumber(), e.getMessage());
            } catch (Exception e) {
                errors.add("处理资产 " + asset.getAssetNumber() + " 时发生意外错误: " + e.getMessage());
                log.error("处理资产 {} 时发生意外错误:", asset.getAssetNumber(), e);
            }
        }

        // 批量更新数据库
        int updatedCount = 0;
        if (!updatedAssets.isEmpty()) {
            boolean success = this.updateBatchById(updatedAssets); // Use ServiceImpl's updateBatchById
            if (success) {
                updatedCount = updatedAssets.size();
            } else {
                 // 如果批量更新部分失败或整体失败，需要更复杂的错误处理。
                 // updateBatchById通常是全成功或全失败（取决于实现），
                 // 但为了健壮性，我们可以收集哪些更新失败。
                 // 目前简单地将批量更新失败作为一个错误报告。
                 errors.add("批量更新资产信息失败");
                 log.error("批量更新资产信息失败，尝试更新 {} 条", updatedAssets.size());
            }
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("updatedCount", updatedCount);
        resultData.put("errors", errors);

        if (!errors.isEmpty()) {
            // Return errors only, as per observed Result.error usage
            return Result.error("部分资产折旧计算或更新时遇到问题");
        } else if (updatedCount > 0) {
             // Return success with data
             return Result.success("成功计算并更新 " + updatedCount + " 项资产的折旧信息", resultData);
        } else {
             // Return success with data even if no updates, indicates process ran
             return Result.success("未找到符合条件的资产或无需更新", resultData);
        }
    }
} 