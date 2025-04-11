package com.project.system2.controller;

import com.project.system2.service.IAssetsService;
import com.project.system2.common.core.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final IAssetsService assetsService;

    @Autowired
    public HomeController(IAssetsService assetsService) {
        this.assetsService = assetsService;
    }


    /**
     * 获取资产按状态统计信息
     * @return 统计结果 (List of {name: status, value: count})
     */
    @GetMapping("/statistics/asset-status")
    public AjaxResult getAssetStatusStatistics() {
        Map<String, Long> statusCounts = assetsService.getAssetStatisticsByStatus();

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            resultList.add(item);
        }
        
        return AjaxResult.success(resultList);
    }

} 