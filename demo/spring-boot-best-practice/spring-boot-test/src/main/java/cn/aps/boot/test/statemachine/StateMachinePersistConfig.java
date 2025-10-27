/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine;

import cn.aps.boot.test.statemachine.model.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StateMachinePersistConfig {

    @Bean
    public StateMachinePersister<PaymentState, PaymentEvent, Payment> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<PaymentState, PaymentEvent, Payment>() {
            private final Map<Integer, StateMachineContext<PaymentState, PaymentEvent>> contexts = new HashMap<>();

            @Override
            public void write(StateMachineContext<PaymentState, PaymentEvent> context, Payment payment) {
                contexts.put(payment.getId(), context);
            }

            @Override
            public StateMachineContext<PaymentState, PaymentEvent> read(Payment payment) {
                return contexts.get(payment.getId());
            }
        });
    }
}
