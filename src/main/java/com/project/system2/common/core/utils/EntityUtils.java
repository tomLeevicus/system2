package com.project.system2.common.core.utils;

import com.project.system2.domain.entity.BaseEntity;
import java.util.Date;

/**
 * 实体工具类
 */
public class EntityUtils {
    
    /**
     * 设置创建和更新信息
     *
     * @param entity 实体对象
     * @param isAdd 是否是新增操作
     */
    public static void setCreateAndUpdateInfo(BaseEntity entity, boolean isAdd) {
        Long userId = SecurityUtils.getUserId();
        // 如果获取不到用户ID，默认为1（admin）
        if (userId == null) {
            userId = 1L;
        }
        Date now = new Date();
        entity.setDelFlag(0);
        if (isAdd) {
            entity.setCreateBy(userId);
            entity.setCreateTime(now);
        }else {
            entity.setUpdateBy(userId);
            entity.setUpdateTime(now);
        }
    }
} 