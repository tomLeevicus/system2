package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysNotice;
import com.project.system2.domain.entity.SysNoticeAttachment;
import com.project.system2.domain.query.SysNoticeQuery;
import com.project.system2.mapper.SysNoticeAttachmentMapper;
import com.project.system2.mapper.SysNoticeMapper;
import com.project.system2.service.ISysNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知公告 服务实现
 */
@Slf4j
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    @Autowired
    private SysNoticeAttachmentMapper noticeAttachmentMapper;

    @Override
    public Page<SysNotice> listNotices(SysNoticeQuery query) {
        // 构建分页对象
        Page<SysNotice> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<SysNotice>()
            .like(query.getNoticeTitle() != null, SysNotice::getNoticeTitle, query.getNoticeTitle())
            .eq(query.getNoticeType() != null, SysNotice::getNoticeType, query.getNoticeType())
            .eq(query.getStatus() != null, SysNotice::getStatus, query.getStatus())
            .eq(query.getCreateBy() != null, SysNotice::getCreateBy, query.getCreateBy())
            .ge(query.getCreateTimeBegin() != null, SysNotice::getCreateTime, query.getCreateTimeBegin())
            .le(query.getCreateTimeEnd() != null, SysNotice::getCreateTime, query.getCreateTimeEnd())
            .orderByDesc(SysNotice::getCreateTime);
        
        // 执行查询
        Page<SysNotice> result = page(page, wrapper);
        
        // 查询附件信息
        result.getRecords().forEach(notice -> {
            List<SysNoticeAttachment> attachments = noticeAttachmentMapper.selectList(
                new LambdaQueryWrapper<SysNoticeAttachment>()
                    .eq(SysNoticeAttachment::getNoticeId, notice.getNoticeId())
            );
            notice.setAttachments(attachments);
        });
        
        return result;
    }

    @Override
    public SysNotice getNoticeById(Long noticeId) {
        // 查询通知信息
        SysNotice notice = getById(noticeId);
        if (notice != null) {
            // 查询附件信息
            List<SysNoticeAttachment> attachments = noticeAttachmentMapper.selectList(
                new LambdaQueryWrapper<SysNoticeAttachment>()
                    .eq(SysNoticeAttachment::getNoticeId, noticeId)
            );
            notice.setAttachments(attachments);
        }
        return notice;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNotice(SysNotice notice) {
        // 设置创建人信息
        EntityUtils.setCreateAndUpdateInfo(notice,true);
        
        // 保存通知信息
        boolean result = save(notice);
        
        // 保存附件信息
        if (result && notice.getAttachments() != null) {
            notice.getAttachments().forEach(attachment -> {
                attachment.setNoticeId(notice.getNoticeId());
                noticeAttachmentMapper.insert(attachment);
            });
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotice(SysNotice notice) {
        // 更新通知信息
        boolean result = updateById(notice);
        
        // 更新附件信息
        if (result && notice.getAttachments() != null) {
            // 删除原有附件
            noticeAttachmentMapper.delete(
                new LambdaQueryWrapper<SysNoticeAttachment>()
                    .eq(SysNoticeAttachment::getNoticeId, notice.getNoticeId())
            );
            
            // 保存新附件
            notice.getAttachments().forEach(attachment -> {
                attachment.setNoticeId(notice.getNoticeId());
                noticeAttachmentMapper.insert(attachment);
            });
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNoticeById(Long noticeId) {
        // 删除附件
        noticeAttachmentMapper.delete(
            new LambdaQueryWrapper<SysNoticeAttachment>()
                .eq(SysNoticeAttachment::getNoticeId, noticeId)
        );
        
        // 删除通知
        return removeById(noticeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNoticeByIds(List<Long> noticeIds) {
        // 删除附件
        noticeAttachmentMapper.delete(
            new LambdaQueryWrapper<SysNoticeAttachment>()
                .in(SysNoticeAttachment::getNoticeId, noticeIds)
        );
        
        // 删除通知
        return removeByIds(noticeIds);
    }
} 