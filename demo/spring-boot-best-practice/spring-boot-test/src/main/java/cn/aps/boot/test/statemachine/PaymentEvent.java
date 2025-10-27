/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine;

/**
 * 支付事件
 */
public enum PaymentEvent {
    /**
     * 支付
     */
    PAY,
    /**
     * 收款
     */
    RECEIVE,
    /**
     * 关闭
     */
    CLOSE
}
