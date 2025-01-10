package cn.aps.boot.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Author : lishirui
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "javastack")
public class User {

    private long id;

    private String name;

    private int sex;

}
