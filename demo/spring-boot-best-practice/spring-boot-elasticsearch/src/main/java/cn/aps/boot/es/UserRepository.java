package cn.aps.boot.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Author : lishirui
 */
public interface UserRepository extends ElasticsearchRepository<User, Long> {

    List<User> findByName(String name);

}

