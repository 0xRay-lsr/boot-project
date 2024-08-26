package cn.lsr.boot.demo.provider.mapper;

import cn.lsr.boot.demo.provider.entity.Accounts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 账户信息表 Mapper 接口
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@Mapper
public interface AccountsMapper extends BaseMapper<Accounts> {

}
