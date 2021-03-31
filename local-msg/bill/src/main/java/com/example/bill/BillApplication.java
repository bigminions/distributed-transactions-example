package com.example.bill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class BillApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillApplication.class, args);
	}

}
