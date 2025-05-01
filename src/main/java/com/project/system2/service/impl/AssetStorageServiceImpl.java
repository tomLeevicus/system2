package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetStorage;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.query.AssetStorageQuery;
import com.project.system2.mapper.AssetStorageMapper;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetStorageService;
import com.project.system2.service.IAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.project.system2.common.core.utils.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AssetStorageServiceImpl extends ServiceImpl<AssetStorageMapper, AssetStorage> implements IAssetStorageService {

    @Autowired
    private AssetStorageMapper assetStorageMapper;

    @Autowired
    private IAssetsService assetsService;

    @Override
    public Result<IPage<AssetStorage>> queryList(AssetStorageQuery query) {
        Page<AssetStorage> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<AssetStorage> wrapper = new LambdaQueryWrapper<>();

        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(AssetStorage::getAssetName, query.getAssetName());
        }

        // 添加操作者名称查询条件
        if (StringUtils.isNotBlank(query.getOperatorName())) {
            wrapper.like(AssetStorage::getOperatorName, query.getOperatorName());
        }

        // 添加入库时间范围查询条件
        if (query.getWarehouseTimeStart() != null && query.getWarehouseTimeEnd() != null) {
            wrapper.between(AssetStorage::getWarehouseTime,
                    query.getWarehouseTimeStart(),
                    query.getWarehouseTimeEnd());
        }

        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("warehouseTime".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()),
                        AssetStorage::getWarehouseTime);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(AssetStorage::getWarehouseTime);
        }

        return Result.success(page(page, wrapper));
    }

    @Override
    public Result<AssetStorage> getById(Long id) {
        AssetStorage assetStorage = assetStorageMapper.selectById(id);
        return Result.success(assetStorage);
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetStorage assetStorage) {
        EntityUtils.setCreateAndUpdateInfo(assetStorage, true);
        int rows = assetStorageMapper.insert(assetStorage);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetStorage assetStorage) {
        EntityUtils.setCreateAndUpdateInfo(assetStorage, false);
        int rows = assetStorageMapper.updateById(assetStorage);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {
        int rows = assetStorageMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional // 确保整个批量操作的原子性
    public Result<Boolean> approveStorageBatch(List<Long> storageIds) {
        List<Assets> allNewAssetsList = new ArrayList<>(); // 用于收集所有新资产

        for (Long storageId : storageIds) { // 循环处理每个 ID
            AssetStorage assetStorage = assetStorageMapper.selectById(storageId);
            if (assetStorage == null) {
                // 记录错误或抛出异常，取决于业务需求，这里选择记录并继续处理下一个
                continue; // 处理下一个 ID
                // 或者可以选择中断整个操作: throw new RuntimeException("未找到入库记录，ID：" + storageId);
            }

            Integer quantity = assetStorage.getInboundQuantity();
            if (quantity == null || quantity <= 0) {
                continue;
            }

            for (int i = 0; i < quantity; i++) {
                Assets newAsset = new Assets();

                // 直接从 assetStorage 获取信息
                newAsset.setAssetName(assetStorage.getAssetName());
                newAsset.setAssetPurchaseTime(assetStorage.getWarehouseTime() != null ? assetStorage.getWarehouseTime() : new Date());

                // 从 assetStorage 获取补充的字段信息
                newAsset.setAssetClassificationId(assetStorage.getAssetClassificationId()); // 使用入库时指定的分类
                newAsset.setAssetModel(assetStorage.getAssetModel()); // 使用入库时指定的型号
                newAsset.setAssetPriceNum(assetStorage.getAssetPriceNum()); // 使用入库时指定的单价
                newAsset.setAssetPriceUnit(assetStorage.getAssetPriceUnit()); // 使用入库时指定的价格单位
                newAsset.setAssetUserId(assetStorage.getAssetUserId()); // 使用入库时指定的负责人
                newAsset.setAssetUseDepartmentId(assetStorage.getAssetUseDepartmentId()); // 使用入库时指定的使用部门
                newAsset.setAssetStorageLocation(assetStorage.getAssetStorageLocation()); // 使用入库时指定的存放地

                // Assets 特有的字段
                newAsset.setAssetUseTime(null); // 启用时间通常在资产被领用或启用时设置，初始为 null

                // 资产编号生成
                newAsset.setAssetNumber(generateUniqueAssetNumber(assetStorage.getAssetName(), i + 1));
                newAsset.setAssetUseStatus(0); // 0: 未启用 (或 闲置)
                newAsset.setAssetStatus(0);    // 0: 正常

                // 设置创建和更新信息
                EntityUtils.setCreateAndUpdateInfo(newAsset, true);

                allNewAssetsList.add(newAsset); // 添加到总列表
            }

            // (可选) 更新单个入库记录状态？ 如果需要，可以在这里更新
            // assetStorage.setStatus("APPROVED"); // 假设有 status 字段
            // EntityUtils.setCreateAndUpdateInfo(assetStorage, false);
            // assetStorageMapper.updateById(assetStorage);
        }

        // 批量保存所有新资产
        if (!allNewAssetsList.isEmpty()) {
            boolean saveResult = assetsService.saveBatchAssets(allNewAssetsList); // 调用批量保存
            if (!saveResult) {
                throw new RuntimeException("批量保存新资产失败"); // 触发事务回滚
            }
        } else {
            // 如果所有记录都因为错误被跳过，这里可能需要返回一个特定的消息
            return Result.error("警告：没有成功处理任何入库记录，请检查日志。");
        }

        // (可选) 批量更新所有处理成功的入库记录状态？

        return Result.success(true);
    }

// generateUniqueAssetNumber 方法保持不变

    /**
     * 生成唯一的资产编号 (示例实现)
     * 注意：在高并发下，仅依赖时间戳可能不足以保证绝对唯一，可能需要数据库序列或其他机制。
     *
     * @param assetName 资产名称 (用于生成前缀)
     * @param index     序号 (在单次入库内的序号)
     * @return 唯一资产编号
     */
    private String generateUniqueAssetNumber(String assetName, int index) {
        // 使用资产名称（或固定前缀） + 时间戳 + 序号
        String prefix = StringUtils.isNotBlank(assetName) ? assetName.replaceAll("[^a-zA-Z0-9]", "").toUpperCase() : "ASSET"; // 简单处理名称作为前缀
        prefix = prefix.length() > 10 ? prefix.substring(0, 10) : prefix; // 限制前缀长度
        long timestamp = System.currentTimeMillis();
        // 考虑加入 storageId 或其他信息增强唯一性
        // 格式可以调整，例如 %s-%d-%04d (时间戳, 序号)
        return String.format("%s-%d-%03d", prefix, timestamp, index);
    }
} 