package cn.aps.boot.test.controller;

import cn.aps.boot.test.annotation.Timeout;
import cn.aps.boot.test.entity.User;
import cn.aps.boot.test.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用于演示超时机制的Controller
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@RestController
@RequestMapping("/api")
public class TimeoutDemoController {

    private final UserRepository userRepository;

    public TimeoutDemoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 正常请求，应该在1秒内返回
     */
    @GetMapping("/fast")
    public String fastEndpoint() {
        System.out.println("ok");
        return "Request processed quickly!";
    }

    /**
     * 模拟一个耗时2秒的任务，但超时设置为1秒。
     * 将会触发超时异常。
     */
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @GetMapping("/slow-but-interruptible")
    public String slowButInterruptibleEndpoint() {
        System.out.println("Entering slow endpoint, will sleep for 2 seconds...");
        try {
            // Thread.sleep是可中断的，当AOP切面调用future.cancel(true)时，这里会抛出InterruptedException
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // 捕获中断异常，转换为运行时异常，避免污染方法签名
            // 同时，重新设置中断状态，以便上层调用者可以感知到中断
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
        System.out.println("Sleep finished (should not be reached).");
        return "Slow request processed.";
    }

    /**
     * 模拟一个死循环任务，超时设置为1秒。
     * 这个例子展示了如何通过检查中断标志来优雅地终止死循环。
     */
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @GetMapping("/infinite-loop")
    public String infiniteLoopEndpoint() {
        System.out.println("Entering infinite loop...");
        long counter = 0;
        while (true) {
            // 关键：在循环中检查当前线程的中断状态
            // 当AOP切面调用future.cancel(true)时，这个标志位被设为true
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Loop interrupted by timeout mechanism!");
                // 收到中断信号，抛出异常或直接返回，以终止方法
                throw new RuntimeException("The loop was interrupted due to a timeout.");
            }
            counter++;
            // 只是为了让CPU不至于100%，实际业务中可能是复杂的计算
            if (counter % 1_000_000_000 == 0) {
                System.out.println("Loop is still running...");
            }
        }
    }

    /**
     * 演示事务回滚。该方法会先向数据库插入一条用户记录，
     * 然后进入一个长时间等待，最终导致超时。
     * 由于超时会中断线程并抛出异常，Spring的事务管理器会捕获异常并回滚事务。
     * 因此，插入的用户记录将不会被持久化。
     */
    @GetMapping("/transactional-timeout")
    @Transactional
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    public String transactionalTimeout() {
        System.out.println("Entering transactional timeout endpoint...");
        System.out.println("Saving a new user 'test-user' to the database.");
        userRepository.save(new User("test-user"));

        System.out.println("User saved within transaction. Now sleeping for 3 seconds to trigger timeout...");
        try {
            // 等待3秒，超过了2秒的超时限制
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during transaction", e);
        }

        System.out.println("This line should never be reached.");
        return "This should not be returned.";
    }

    /**
     * 用于验证事务回滚是否成功
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
