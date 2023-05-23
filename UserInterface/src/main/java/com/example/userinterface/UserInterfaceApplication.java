package com.example.userinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 18000)
public class UserInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserInterfaceApplication.class, args);
	}

}
