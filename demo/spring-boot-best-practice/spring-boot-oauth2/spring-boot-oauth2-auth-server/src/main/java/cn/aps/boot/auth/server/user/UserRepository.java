package cn.aps.boot.auth.server.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/23 16:49
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}