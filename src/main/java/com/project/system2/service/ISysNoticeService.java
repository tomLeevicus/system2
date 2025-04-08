package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.system2.domain.entity.SysNotice;
import com.project.system2.domain.query.SysNoticeQuery;

import java.util.List;

/**
 * 通知公告 服务层
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询通知公告列表
     *
     * @param query 查询条件
     * @return 通知公告分页列表
     */
    Page<SysNotice> listNotices(SysNoticeQuery query);

    /**
     * 查询通知公告信息
     *
     * @param noticeId 通知公告ID
     * @return 通知公告信息
     */
    SysNotice getNoticeById(Long noticeId);

    /**
     * 新增通知公告
     *
     * @param notice 通知公告信息
     * @return 结果
     */
    boolean addNotice(SysNotice notice);

    /**
     * 修改通知公告
     *
     * @param notice 通知公告信息
     * @return 结果
     */
    boolean updateNotice(SysNotice notice);

    /**
     * 删除通知公告信息
     *
     * @param noticeId 通知公告ID
     * @return 结果
     */
    boolean deleteNoticeById(Long noticeId);

    /**
     * 批量删除通知公告信息
     *
     * @param noticeIds 需要删除的通知公告ID
     * @return 结果
     */
    boolean deleteNoticeByIds(List<Long> noticeIds);
} 