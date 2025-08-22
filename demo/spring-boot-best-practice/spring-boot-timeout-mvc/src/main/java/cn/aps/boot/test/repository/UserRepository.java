package cn.aps.boot.test.repository;

import cn.aps.boot.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户Repository
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
