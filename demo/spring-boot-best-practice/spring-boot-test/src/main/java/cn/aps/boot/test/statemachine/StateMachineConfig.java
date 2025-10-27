/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    /**
     * 配置状态
     * @param states a state machine state configurer
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states
            .withStates()
            .initial(PaymentState.UNPAID)
            .states(EnumSet.allOf(PaymentState.class));
    }

    /**
     * 配置状态转换
     * @param transitions a state machine transition configurer
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(PaymentState.UNPAID).target(PaymentState.WAITING_FOR_RECEIVE)
                .event(PaymentEvent.PAY)
                .and()
            .withExternal()
                .source(PaymentState.WAITING_FOR_RECEIVE).target(PaymentState.SUCCESS)
                .event(PaymentEvent.RECEIVE)
                .and()
            .withExternal()
                .source(PaymentState.UNPAID).target(PaymentState.CLOSED)
                .event(PaymentEvent.CLOSE)
        ;
    }
}
