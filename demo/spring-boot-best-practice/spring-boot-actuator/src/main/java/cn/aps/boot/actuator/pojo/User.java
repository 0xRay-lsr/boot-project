package cn.aps.boot.actuator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author : lishirui
 */
@Data
@AllArgsConstructor
public class User {

    private int id;

    private String name;

    private int age;

}