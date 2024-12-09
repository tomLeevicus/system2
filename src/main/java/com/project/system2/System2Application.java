package com.project.system2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.project.system2.mapper")
@EnableCaching
public class System2Application {

	public static void main(String[] args) {
		SpringApplication.run(System2Application.class, args);
	}

}
