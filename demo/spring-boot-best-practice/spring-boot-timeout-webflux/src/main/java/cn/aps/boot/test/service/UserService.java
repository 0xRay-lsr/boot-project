package cn.aps.boot.test.service;

import cn.aps.boot.test.entity.User;
import cn.aps.boot.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 用户服务，包含核心业务逻辑 (最终净化版)
 * @author lishirui
 * @date 2025-08-21
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建一个新用户。
     * 这里的代码是纯粹的业务逻辑，无需关心任何超时或异常处理的细节。
     * @param user 要创建的用户对象
     * @return 包含创建后用户的Mono
     */
    @Transactional
    public Mono<User> createUser(User user) {
        // 使用Mono.fromCallable来包装可能产生阻塞的业务代码
        return Mono.fromCallable(() -> {
            System.out.println("业务线程: " + Thread.currentThread().getName() + " - 开始执行业务逻辑...");

            // 1. 模拟一个耗时5秒的业务操作
            // 假设这是一个很慢的数据库查询或外部API调用
            // 注意：这里不再有 try-catch(InterruptedException)
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // 即使我们在这里捕获并重新抛出，最终效果与不捕获一致。
                // 为了代码整洁，业务开发者完全可以忽略它。
                // InterruptedException会自然地被Mono.fromCallable转换为onError信号。
                throw new RuntimeException(e);
            }

            // 2. 执行真正的数据库保存操作
            User savedUser = userRepository.save(user);

            System.out.println("业务线程: " + Thread.currentThread().getName() + " - 业务逻辑执行完毕。");
            return savedUser;
        })
        // 将阻塞调用切换到工作线程池
        .subscribeOn(Schedulers.boundedElastic());
    }
}
