package com.chat.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.robot.entity.bo.AiChatMemory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hanson.huang
 * @version V1.0
 * @InterfaceName AiChatMemoryMapper
 * @date 2025/7/24 11:21
 **/
@Mapper
public interface AiChatMemoryMapper extends BaseMapper<AiChatMemory> {

    @Insert("<script>" +
            "INSERT INTO ai_chat_memory (conversation_id, type, content) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.conversationId}, #{item.type}, #{item.content})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<AiChatMemory> list);
}
