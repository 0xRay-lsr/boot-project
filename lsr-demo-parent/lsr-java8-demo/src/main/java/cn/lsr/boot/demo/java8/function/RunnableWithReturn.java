package cn.lsr.boot.demo.java8.function;

/**
 * @Description : 参考源码 Supplier 定义一个
 * @Author : lishirui
 * @Date ：2024/8/26 17:13
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface RunnableWithReturn<R> {
    public R execute();
}
