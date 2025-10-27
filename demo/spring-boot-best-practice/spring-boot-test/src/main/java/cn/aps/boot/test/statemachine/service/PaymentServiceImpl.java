/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine.service;

import cn.aps.boot.test.statemachine.PaymentEvent;
import cn.aps.boot.test.statemachine.PaymentState;
import cn.aps.boot.test.statemachine.model.Payment;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    @Resource
    private StateMachinePersister<PaymentState, PaymentEvent, Payment> stateMachinePersister;

    // 模拟数据库
    private final Map<Integer, Payment> paymentMap = new HashMap<>();
    private int id = 1;

    public PaymentServiceImpl() {
        paymentMap.put(id, new Payment(id, PaymentState.UNPAID));
    }

    @Override
    public Payment pay(Integer id) {
        Payment payment = paymentMap.get(id);
        if (payment == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!sendEvent(PaymentEvent.PAY, payment)) {
            throw new RuntimeException("支付失败, 状态异常");
        }
        return payment;
    }

    @Override
    public Payment receive(Integer id) {
        Payment payment = paymentMap.get(id);
        if (payment == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!sendEvent(PaymentEvent.RECEIVE, payment)) {
            throw new RuntimeException("确认收货失败, 状态异常");
        }
        return payment;
    }

    @Override
    public Payment close(Integer id) {
        Payment payment = paymentMap.get(id);
        if (payment == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!sendEvent(PaymentEvent.CLOSE, payment)) {
            throw new RuntimeException("关闭订单失败, 状态异常");
        }
        return payment;
    }

    /**
     * 发送事件
     *
     * @param event
     * @param payment
     * @return
     */
    private boolean sendEvent(PaymentEvent event, Payment payment) {
        synchronized (payment) {
            boolean result = false;
            StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine();
            try {
                // 从持久化中恢复状态机
                stateMachinePersister.restore(stateMachine, payment);
                Message<PaymentEvent> message = MessageBuilder.withPayload(event).setHeader("payment", payment).build();
                result = stateMachine.sendEvent(message);
                if (result) {
                    // 持久化状态机
                    stateMachinePersister.persist(stateMachine, payment);
                    payment.setStatus(stateMachine.getState().getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stateMachine.stop();
            }
            return result;
        }
    }
}
