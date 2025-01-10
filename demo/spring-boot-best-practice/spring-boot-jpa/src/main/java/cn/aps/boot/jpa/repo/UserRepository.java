package cn.aps.boot.jpa.repo;

import cn.aps.boot.jpa.entity.UserDO;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDO, Long> {

}

