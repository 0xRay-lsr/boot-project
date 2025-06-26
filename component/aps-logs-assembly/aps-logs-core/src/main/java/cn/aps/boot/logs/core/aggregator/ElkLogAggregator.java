package cn.aps.boot.logs.core.aggregator;

import cn.aps.boot.logs.api.aggregator.LogEvent;
import cn.aps.boot.logs.api.config.AggregatorConfig;
import cn.aps.boot.logs.api.spi.SPIMeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.util.Map;

/**
 * @Description : ELK日志聚合器实现
 * @Author : lishirui
 * @Date ：2025/4/14 15:31
 */
@SPIMeta(id = "elk")
public class ElkLogAggregator extends AbstractLogAggregator {

    private RestHighLevelClient client;
    private ObjectMapper objectMapper;
    private String host = "localhost";
    private int port = 9200;
    private String indexPrefix = "logs";

    @Override
    public void init(Map<String, String> config) {
        super.init(config);
        // 从配置中读取ELK相关参数
        host = config.getOrDefault("elasticsearch.host", "localhost");
        port = Integer.parseInt(config.getOrDefault("elasticsearch.port", "9200"));
        indexPrefix = config.getOrDefault("elasticsearch.indexPrefix", "logs");
        initClient();
    }

    @Override
    public void init(AggregatorConfig config) {
        super.init(config);
        host = config.getElkConfig().getHost();
        port = config.getElkConfig().getPort();
        indexPrefix = config.getElkConfig().getIndexPrefix();
        initClient();
    }

    private void initClient() {
        // 初始化ES客户端
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );

        objectMapper = new ObjectMapper();
    }

    @Override
    protected void processEvent(LogEvent event) {
        try {
            String indexName = indexPrefix + "-" + event.getTimestamp().toLocalDate().toString();
            IndexRequest request = new IndexRequest(indexName);

            // 将日志事件转换为JSON
            String json = objectMapper.writeValueAsString(event);
            request.source(json, XContentType.JSON);

            // 发送到ES
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 