package cn.aps.boot.mybatisplus.service;

import cn.aps.boot.mybatisplus.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author : lishirui
 */
public interface UserService extends IService<UserDO> {

    UserDO getByUsername(String username, int type);

}