package cn.aps.boot.demo.java8.abs;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/8/29 10:12
 */
public class WeixinPay extends PayAbstract {
    @Override
    void doInitPlay() {
        System.out.println("初始化微信支付逻辑。。。");
    }

    @Override
    void doSendPay(String src, String dest, int money) {
        System.out.println("微信支付转账逻辑。。。");
    }
}
