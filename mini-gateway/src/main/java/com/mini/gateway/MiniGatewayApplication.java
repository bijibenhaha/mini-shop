package com.mini.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MiniGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniGatewayApplication.class, args);
    }
}