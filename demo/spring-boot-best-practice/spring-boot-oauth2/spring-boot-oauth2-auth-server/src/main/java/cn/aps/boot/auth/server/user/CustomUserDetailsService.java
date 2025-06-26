package cn.aps.boot.auth.server.user;

import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <--- 确保此行导入
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description : 自定义用户详情服务 用于从数据库加载用户信息供 Spring Security 使用
 * @Author : lishirui
 * @Date ：2025/6/25 10:03
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;

    /**
     * 根据用户名加载用户详情
     *
     * @param username 用户名
     * @return UserDetails 对象，包含用户名、密码和权限
     * @throws UsernameNotFoundException 如果用户不存在则抛出异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        // 将用户角色字符串转换为 Spring Security 的 GrantedAuthority 列表
        List<GrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new) // 为每个角色创建一个SimpleGrantedAuthority
                .collect(Collectors.toList());
        // 构建并返回 Spring Security 的 UserDetails 对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // 用户名
                user.getPassword(), // 密码
                authorities         // 权限列表
        );
    }
}
