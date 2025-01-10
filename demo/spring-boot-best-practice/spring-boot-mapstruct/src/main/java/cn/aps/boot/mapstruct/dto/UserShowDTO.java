package cn.aps.boot.mapstruct.dto;

import lombok.Data;

/**
 * @Author : lishirui
 * 
 */
@Data
public class UserShowDTO {

    private String name;

    private int sex;

    private boolean married;

    private String birthday;

    private String regDate;

    private String registerSource;

    private String favorite;

    private String memo;

}
