package cn.lsr.boot.demo.java8.abs;

/**
 * @Description : 支付接口
 * @Author : lishirui
 * @Date ：2024/8/26 16:48
 */
public interface Pay {
    /**
     * 初始化支付环境
     */
    public void initPay();

    /**
     * 发起转账
     * @param src 发起人
     * @param dest 目前人
     * @param money 转账金额
     */
    public void sendPay(String src, String dest, int money);

    /**
     * 通知
     */
    default void notifily(){
        System.out.println("默认实现通知。。。。");
    }

}
