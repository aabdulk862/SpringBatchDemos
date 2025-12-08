package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo10DbFileItemwriterApplication {

	public static void main(String[] args) {


		String[] jobParams = new String[] {"outputFile=OutputFile/employees.csv"};
		SpringApplication.run(Demo10DbFileItemwriterApplication.class, jobParams);
	}

}
