package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo13FlatfileDbItemwriterApplication {

	public static void main(String[] args) {
		String[] jobParams = new String[] {"inputFile=InputFile/employees.csv"};
		SpringApplication.run(Demo13FlatfileDbItemwriterApplication.class, jobParams);
	}

}
