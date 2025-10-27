/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine;

/**
 * 支付状态
 */
public enum PaymentState {
    /**
     * 待支付
     */
    UNPAID,
    /**
     * 待收款
     */
    WAITING_FOR_RECEIVE,
    /**
     * 转账成功
     */
    SUCCESS,
    /**
     * 转账关闭
     */
    CLOSED;
}
