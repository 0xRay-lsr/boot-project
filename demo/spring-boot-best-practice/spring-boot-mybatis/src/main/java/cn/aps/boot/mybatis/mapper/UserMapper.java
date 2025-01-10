package cn.aps.boot.mybatis.mapper;


import cn.aps.boot.mybatis.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author : lishirui
 */
@Mapper
public interface UserMapper {

    UserDO findById(@Param("id") long id);

}




