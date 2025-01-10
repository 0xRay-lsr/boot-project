package cn.aps.boot.mybatisplus.mapper;

import cn.aps.boot.mybatisplus.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author : lishirui
 */
public interface UserMapper extends BaseMapper<UserDO> {

    UserDO selectByUsername(@Param("username") String username);

}