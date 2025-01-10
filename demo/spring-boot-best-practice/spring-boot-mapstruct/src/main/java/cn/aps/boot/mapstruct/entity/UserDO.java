package cn.aps.boot.mapstruct.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author : lishirui
 * 
 */
@Data
public class UserDO {

    private String name;

    private int sex;

    private int age;

    private Date birthday;

    private String phone;

    private boolean married;

    private Date regDate;

    private Date loginDate;

    private String memo;

    private UserExtDO userExtDO;


}
