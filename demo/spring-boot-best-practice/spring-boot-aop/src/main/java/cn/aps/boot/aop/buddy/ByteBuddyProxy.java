package cn.aps.boot.aop.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @Description : ByteBuddy代理工具类
 * @Author : lishirui
 * @Date ：2025/5/12 16:41
 */
public class ByteBuddyProxy {

    /**
     * 创建代理对象
     * @param targetClass 目标类
     * @param methodName 要增强的方法名
     * @param before 前置增强逻辑
     * @param after 后置增强逻辑
     * @param <T> 目标类型
     * @return 代理对象
     * @throws Exception
     */
    public static <T> T createProxy(Class<T> targetClass, String methodName, 
                                  Runnable before, Runnable after) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class<? extends T> proxyClass = new ByteBuddy()
                .subclass(targetClass)
                .method(ElementMatchers.named(methodName))
                .intercept(MethodDelegation.to(new Interceptor(before, after)))
                .make()
                .load(targetClass.getClassLoader())
                .getLoaded();

        return proxyClass.getDeclaredConstructor().newInstance();
    }

    public static class Interceptor {
        private final Runnable before;
        private final Runnable after;

        public Interceptor(Runnable before, Runnable after) {
            this.before = before;
            this.after = after;
        }

        public void intercept(@SuperCall Callable<?> originalMethod) throws Exception {
            if (before != null) {
                before.run();
            }
            try {
                // 这里需要实际调用目标方法
                // 对于接口代理，可以使用InvocationHandler配合MethodHandle
                originalMethod.call();
            }finally {
                if (after != null) {
                    after.run();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // 测试用例
        Runnable before = () -> System.out.println("Before method call");
        Runnable after = () -> System.out.println("After method call");

        // 将TestClass改为public static避免访问权限问题
        TestClass proxy = ByteBuddyProxy.createProxy(TestClass.class, "testMethod", before, after);
        proxy.testMethod();

        
    }

    public static class TestClass {
        public void testMethod() {
            System.out.println("Original method called");
        }
    }
}
