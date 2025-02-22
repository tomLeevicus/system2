package com.project.system2.controller;

import com.project.system2.domain.dto.PersonalAssetDTO;
import com.project.system2.service.IAssetsReceiptService;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: BlueberryLee
 * @Date: 2025/2/22 20:23
 * @Description: TODO 个人资产
 */
@RestController
@RequestMapping("/asset/personalAssets")
public class PersonalAssetsController {

    @Autowired
    private IAssetsReceiptService assetsReceiptService;

    /**
     * 分页获取当前用户资产信息及领用信息
     */
    @GetMapping("/list")
    public Result<IPage<PersonalAssetDTO>> list(@RequestParam int pageNum, @RequestParam int pageSize) {
        Long userId = SecurityUtils.getLoginUser().getUser().getId();
        return assetsReceiptService.getPersonalAssets(userId, pageNum, pageSize);
    }
}
