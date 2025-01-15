package com.project.system2.common.core.utils;

import com.project.system2.domain.entity.SysDictData;
import com.project.system2.service.SysDictDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictUtils {
    
    private static SysDictDataService dictDataService;

    public DictUtils(SysDictDataService dictDataService) {
        DictUtils.dictDataService = dictDataService;
    }

    /**
     * 根据字典类型查询字典数据
     */
    public static List<SysDictData> getDictCache(String dictType) {
        return dictDataService.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        dictDataService.clearDictCache();
    }

    /**
     * 重置字典缓存
     */
    public static void resetDictCache() {
        dictDataService.resetDictCache();
    }
} 