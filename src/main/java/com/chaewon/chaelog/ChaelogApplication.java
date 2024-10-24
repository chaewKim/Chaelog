package com.chaewon.chaelog;

import com.chaewon.chaelog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AppConfig.class)
public class ChaelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaelogApplication.class, args);
	}

}
