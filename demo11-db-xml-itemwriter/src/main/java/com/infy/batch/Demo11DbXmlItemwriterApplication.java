package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo11DbXmlItemwriterApplication {

	public static void main(String[] args) {
		String[] jobParams = new String[] {"outputFile=OutputFile/employees.xml"};
		SpringApplication.run(Demo11DbXmlItemwriterApplication.class, jobParams);
	}

}
