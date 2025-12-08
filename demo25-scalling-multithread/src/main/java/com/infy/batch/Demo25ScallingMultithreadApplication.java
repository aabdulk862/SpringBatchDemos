package com.infy.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Demo25ScallingMultithreadApplication {

	public static void main(String[] args) {
		String[] param= new String[] {"inputFile=InputFile/employees.csv"};
		SpringApplication.run(Demo25ScallingMultithreadApplication.class, param);
	}

}
