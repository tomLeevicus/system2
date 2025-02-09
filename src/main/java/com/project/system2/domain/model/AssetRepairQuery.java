package com.project.system2.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: BlueberryLee
 * @Date: 2025/1/27 13:24
 * @Description: TODO
 */
@Data
public class AssetRepairQuery {
    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 维修状态
     */
    private Integer status;

    /**
     * 报修日期范围-开始
     */
    private Date repairDateStart;

    /**
     * 报修日期范围-结束
     */
    private Date repairDateEnd;
}
