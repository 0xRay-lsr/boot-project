package cn.aps.boot.redis.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author : lishirui
 */
@Data
public class User {

    private int id;

    private String name;

    private Date birthday;

    private List<String> interesting;

    private Map<String, Object> others;

}
