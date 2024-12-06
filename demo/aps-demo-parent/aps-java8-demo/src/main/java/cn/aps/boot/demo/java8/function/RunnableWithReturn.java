package cn.aps.boot.demo.java8.function;

/**
 * @Description : 参考源码 Supplier 定义一个
 * @Author : lishirui
 * @Date ：2024/8/26 17:13
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface RunnableWithReturn<R> {
    /**
     * 生产者
     * @return 返回结果
     */
    public R execute();
}
