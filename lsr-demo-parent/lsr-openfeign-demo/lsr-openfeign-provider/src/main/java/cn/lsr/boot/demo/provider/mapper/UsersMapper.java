package cn.lsr.boot.demo.provider.mapper;

import cn.lsr.boot.demo.provider.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}
