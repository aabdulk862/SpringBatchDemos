package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo12DbJsonItemwriterApplication {

	public static void main(String[] args) {
		String[] jobParams = new String[] {"outputFile=OutputFile/employees.json"};
		SpringApplication.run(Demo12DbJsonItemwriterApplication.class, jobParams);
	}

}
