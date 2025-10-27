/**n * @author lishiruin * @date 2025-10-27n */
package cn.aps.boot.test;

import lombok.Data;

/**
 * @Author : lishirui
 */
@Data
public class Result {

    private int code = 0;

    private String msg;

    private Object data;

}