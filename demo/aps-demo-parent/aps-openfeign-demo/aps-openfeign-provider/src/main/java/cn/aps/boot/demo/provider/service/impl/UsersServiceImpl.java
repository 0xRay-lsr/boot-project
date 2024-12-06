package cn.aps.boot.demo.provider.service.impl;

import cn.aps.boot.demo.provider.entity.Users;
import cn.aps.boot.demo.provider.mapper.UsersMapper;
import cn.aps.boot.demo.provider.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
