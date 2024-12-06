package cn.aps.boot.demo.java8.abs;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/8/29 10:14
 */
public class ZhifubaoPay extends PayAbstract {
    @Override
    void doInitPlay() {
        System.out.println("初始化支付宝支付逻辑。。。");
    }

    @Override
    void doSendPay(String src, String dest, int money) {
        System.out.println("支付宝支付转账逻辑。。。");
    }
}
