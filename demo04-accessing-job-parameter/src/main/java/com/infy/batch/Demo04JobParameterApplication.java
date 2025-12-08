package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo04JobParameterApplication {

	public static void main(String[] args) {
		String[] jobParams = new String[] {"name=Mike"};
		SpringApplication.run(Demo04JobParameterApplication.class, jobParams);
	}

}
