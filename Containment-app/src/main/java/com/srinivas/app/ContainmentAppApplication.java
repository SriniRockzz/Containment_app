package com.srinivas.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@PropertySource(value ="classpath:application.properties")
@SpringBootApplication
public class ContainmentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContainmentAppApplication.class, args);
	}

}
