/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine.service;

import cn.aps.boot.test.statemachine.model.Payment;

public interface PaymentService {
    Payment pay(Integer id);

    Payment receive(Integer id);

    Payment close(Integer id);
}
