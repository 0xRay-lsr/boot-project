package cn.aps.boot.webflux.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @Author : lishirui
 */
@Configuration
public class WebConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3)
                                .doOnConnected(conn -> {
                                    conn.addHandlerLast(new ReadTimeoutHandler(3000));
                                    conn.addHandlerLast(new WriteTimeoutHandler(3000));
                                })
                );
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return webClientBuilder.clientConnector(connector).build();
    }

}
