package com.chat.robot.entity.bo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName AiChatMemory
 * @date 2025/7/24 11:09
 **/
@TableName(value ="ai_chat_memory")
@Data
public class AiChatMemory implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话id
     */
    @TableField("conversation_id")
    private String conversationId;

    /**
     * 消息类型
     */
    @TableField("type")
    private String type;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;

}