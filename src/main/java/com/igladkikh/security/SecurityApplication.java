package com.igladkikh.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.InetAddress;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> helloRouter() {
        return RouterFunctions.route()
                .GET("/hello", request -> {
                    InetAddress address = InetAddress.getLocalHost();
                    return ServerResponse.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body("<h1>Hello from %s with address %s</h1>"
                                    .formatted(address.getHostName(),
                                            address.getHostAddress()));
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> jsonRouter() {
        RestTemplate restTemplate = new RestTemplate();
        return RouterFunctions.route()
                .GET("/json", request -> {
                    ResponseEntity<String> exchange = restTemplate.exchange("https://jsonplaceholder.typicode.com/todos", HttpMethod.GET, HttpEntity.EMPTY, String.class);
                    if (!exchange.getStatusCode().is2xxSuccessful()) {
                        return ServerResponse.badRequest().build();
                    }
                    String body = exchange.getBody();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body == null ? "{}" : body);
                })
                .build();
    }
}
