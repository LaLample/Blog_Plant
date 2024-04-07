package com.lam;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableEurekaClient
@EnableDiscoveryClient //服务发现
@EnableHystrix //添加对熔断的支持
public class Blog_PlantApplication {
    public static void main(String[] args) {
        SpringApplication.run(Blog_PlantApplication.class,args);
    }
}
