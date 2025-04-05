package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.query.AssetReceiptQuery;
import com.project.system2.domain.query.AssetReceiptRecordQuery;

public interface IAssetReceiptService {
    IPage<AssetReceipt> queryList(AssetReceiptQuery query);
    AssetReceipt getById(Long id);
    boolean add(AssetReceipt assetReceipt);
    boolean update(AssetReceipt assetReceipt);
    boolean deleteById(Long id);
    boolean approveReceipt(AssetReceiptRecordQuery query);
} 