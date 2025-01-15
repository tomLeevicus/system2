package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("assets")
public class Assets {
    @TableId
    private Long id;
    private String assetName;
    private String assetType;
    private String status;
    private String location;
    private Date purchaseDate;
    private Double purchasePrice;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String description;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
} 