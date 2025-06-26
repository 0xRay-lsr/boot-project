package cn.aps.boot.aop.asm;

import java.lang.reflect.Method;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/5/12 15:38
 */
public class AsmRunner {
    public static void main(String[] args) throws Exception {
        String className = "cn.aps.boot.aop.asm.EnhancedHelloService";

        MyClassLoader loader = new MyClassLoader();
        Class<?> clazz = loader.findClass(className);

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod("sayHello");
        method.invoke(instance);
    }
}
