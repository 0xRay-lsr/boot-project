package cn.aps.boot.web.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author : lishirui
 */
@Data
@AllArgsConstructor
public class OrderInfo {

    private String orderNo;

    private long amount;

    private Date time;

}
