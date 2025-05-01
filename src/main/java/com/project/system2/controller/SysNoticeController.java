package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysNotice;
import com.project.system2.domain.query.SysNoticeQuery;
import com.project.system2.service.ISysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告 控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/notice")
@Tag(name = "通知公告管理", description = "通知公告管理接口")
public class SysNoticeController {

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    @Operation(summary = "获取通知公告列表", description = "分页查询通知公告列表")
    public Result<Page<SysNotice>> list(@Validated SysNoticeQuery query) {
        return Result.success(noticeService.listNotices(query));
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/get/{noticeId}")
    @Operation(summary = "获取通知公告详情", description = "根据通知公告ID获取详细信息")
    public Result<SysNotice> getInfo(@PathVariable Long noticeId) {
        return Result.success(noticeService.getNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @PostMapping
    @Operation(summary = "新增通知公告", description = "创建新的通知公告")
    public Result<Boolean> add(@Validated @RequestBody SysNotice notice) {
        return Result.success(noticeService.addNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @PutMapping
    @Operation(summary = "修改通知公告", description = "更新通知公告信息")
    public Result<Boolean> edit(@Validated @RequestBody SysNotice notice) {
        return Result.success(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @DeleteMapping("/{noticeId}")
    @Operation(summary = "删除通知公告", description = "删除指定的通知公告")
    public Result<Boolean> remove(@PathVariable Long noticeId) {
        return Result.success(noticeService.deleteNoticeById(noticeId));
    }

    /**
     * 批量删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除通知公告", description = "批量删除通知公告")
    public Result<Boolean> removeBatch(@RequestBody List<Long> noticeIds) {
        return Result.success(noticeService.deleteNoticeByIds(noticeIds));
    }
} 