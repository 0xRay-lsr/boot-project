package cn.aps.boot.demo.java8.abs.factory;

import cn.aps.boot.demo.java8.abs.Pay;
import cn.aps.boot.demo.java8.abs.WeixinPay;
import cn.aps.boot.demo.java8.abs.ZhifubaoPay;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/8/29 10:19
 */
public class PayFactory {
    /**
     * 获取支付实现
     *
     * @param payType 支付类型
     * @return 具体的支付实现类
     */
    public static Pay getPay(PayType payType) {
        if (payType == PayType.WEIXIN) {
            return new WeixinPay();
        } else if (payType == PayType.ZHIFUBAO) {
            return new ZhifubaoPay();
        }
        return null;
    }
}
