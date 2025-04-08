package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.system2.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告附件表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice_attachment")
@Schema(description = "通知公告附件")
public class SysNoticeAttachment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 附件ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "附件ID")
    private Long attachmentId;

    /**
     * 通知ID
     */
    @Schema(description = "通知ID")
    private Long noticeId;

    /**
     * 附件名称
     */
    @Schema(description = "附件名称")
    private String fileName;

    /**
     * 附件路径
     */
    @Schema(description = "附件路径")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;
} 