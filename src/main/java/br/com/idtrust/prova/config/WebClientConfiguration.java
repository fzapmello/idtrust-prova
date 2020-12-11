package br.com.idtrust.prova.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.UUID;

@Component
public class WebClientConfiguration {

    private static final String BASE_URL = "https://www.quandl.com/api/v3/datasets/CEPEA/";

    public static final String API_KEY_PROPERTY = "API_KEY";
    public static final String CEPEA_API_URL_PROPERTY = "CEPEA_API_URL";
    public static final String MOEDAS_API_URL_PROPERTY = "MOEDAS_API_URL";
    public static final String URI_MOEDA_USD_BRL = "USD-BRL";


    @Bean
    public WebClient defaultWebClient() {

        var tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(2))
                                .addHandlerLast(new WriteTimeoutHandler(2)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultCookie("cookieKey", "cookieValue", "xxx", "xxx")
                .defaultCookie("secretToken", UUID.randomUUID().toString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "I'm a teapot")
                .build();
    }
}