package com.chat.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.robot.entity.bo.Logger;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hanson.huang
 * @version V1.0
 * @InterfaceName LoggerMapper
 * @date 2025/7/24 17:11
 **/
@Mapper
public interface LoggerMapper extends BaseMapper<Logger> {
}
