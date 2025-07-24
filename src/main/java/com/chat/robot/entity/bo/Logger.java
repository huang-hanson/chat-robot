package com.chat.robot.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName Logger
 * @Description TODO
 * @date 2025/7/24 17:09
 **/
@Data
@TableName(value ="logger")
public class Logger {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("userId")
    private Long userId;

    /**
     * 消息内容
     */
    @TableField("message")
    private String message;

    /**
     * 时间戳，默认当前时间
     */
    @TableField(value = "time", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime time;
}