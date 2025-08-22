package cn.aps.boot.test.controller;

import cn.aps.boot.test.annotation.Timeout;
import cn.aps.boot.test.entity.User;
import cn.aps.boot.test.repository.UserRepository;
import cn.aps.boot.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关的WebFlux接口
 * (所有异常处理逻辑已移至WebExceptionHandler)
 * @author lishirui
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/api")
public class TimeoutDemoController {

    @Autowired
    private UserRepository userRepository; // 新增：用于演示的数据库操作

    /**
     * 正常请求，应该在1秒内返回
     */
    @GetMapping("/fast")
    public Mono<String> fastEndpoint() {
        System.out.println("ok");
        return Mono.just("Request processed quickly!");
    }

    /**
     * 模拟一个耗时2秒的任务，但超时设置为1秒。
     * 将会触发超时异常。
     */
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @GetMapping("/slow-but-interruptible")
    public Mono<String> slowButInterruptibleEndpoint() {
        System.out.println("Entering slow endpoint, will sleep for 2 seconds...");
        return Mono.fromCallable(() -> {
            try {
                Thread.sleep(2000); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 重新设置中断状态
                throw new RuntimeException("Thread was interrupted", e); // 抛出运行时异常，由WebExceptionHandler处理
            }
            System.out.println("Sleep finished (should not be reached).");
            return "Slow request processed.";
        }).subscribeOn(Schedulers.boundedElastic()); // 切换到弹性线程池
    }

    /**
     * 模拟一个长时间运行的任务，超时设置为1秒。
     * 这个例子展示了如何通过响应式操作符可靠地触发超时。
     * （使用 while(true) 循环）在WebFlux中无法可靠地被中断，因为它是一个CPU密集型任务，Schedulers.boundedElastic() 不会强制中断它。
     */
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @GetMapping("/infinite-loop") //
    public Mono<String> longRunningEndpoint() {
        System.out.println("Entering infinite loop  will delay for 5 seconds...");
        // 使用Mono.delay模拟一个长时间运行的任务，它在超时时会被可靠地中断
        return Mono.delay(Duration.ofSeconds(5))
                .map(l -> "Long running task finished.")
                .doOnCancel(() -> System.out.println("--- longRunningEndpoint: Task cancelled by timeout! ---"));
    }

    /**
     * 演示事务回滚。该方法会先向数据库插入一条用户记录，
     * 然后进入一个长时间等待，最终导致超时。
     * 由于超时会中断线程并抛出异常，Spring的事务管理器会捕获异常并回滚事务。
     * 因此，插入的用户记录将不会被持久化。
     */
    @GetMapping("/transactional-timeout")
    @Transactional // 确保事务生效
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    public Mono<String> transactionalTimeout() {
        System.out.println("Entering transactional timeout endpoint...");
        return Mono.fromCallable(() -> {
            System.out.println("Saving a new user 'test-user' to the database.");
            userRepository.save(new User("test-user"));
            System.out.println("User saved within transaction. Now sleeping for 3 seconds to trigger timeout...");
            try {
                Thread.sleep(3000); // 等待3秒，超过了2秒的超时限制
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted during transaction", e);
            }

            System.out.println("This line should never be reached.");
            return "This should not be returned.";
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 用于验证事务回滚是否成功
     */
    @GetMapping("/users-all") // 避免与/users POST冲突
    public Mono<List<User>> getAllUsers() {
        System.out.println("--- getAllUsers: Fetching all users... ---");
        return Mono.fromCallable(() -> userRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
