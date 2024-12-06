package cn.aps.boot.demo.provider.service.impl;

import cn.aps.boot.demo.provider.entity.Accounts;
import cn.aps.boot.demo.provider.mapper.AccountsMapper;
import cn.aps.boot.demo.provider.service.AccountsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户信息表 服务实现类
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@Service
public class AccountsServiceImpl extends ServiceImpl<AccountsMapper, Accounts> implements AccountsService {

}
