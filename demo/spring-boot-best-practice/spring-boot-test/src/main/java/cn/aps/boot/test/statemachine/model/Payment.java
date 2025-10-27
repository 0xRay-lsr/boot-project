/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.statemachine.model;

import cn.aps.boot.test.statemachine.PaymentState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Integer id;
    private PaymentState status;

}
