package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.dto.PersonalAssetDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetReceiptMapper extends BaseMapper<AssetReceipt> {
    IPage<PersonalAssetDTO> selectPersonalAssets(Page<PersonalAssetDTO> page, @Param("userId") Long userId);

    int updateReviewStatus(@Param("receiptId") Long receiptId,@Param("reviewStatus") int reviewStatus);

}