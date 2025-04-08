package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.SysNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表 数据层
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {
} 