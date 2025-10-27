/**n * @author lishiruin * @date 2025-10-27n */
package cn.aps.boot.test.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author : lishirui
 */
@Data
@AllArgsConstructor
public class User {

    private long id;

    private String name;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;

}
