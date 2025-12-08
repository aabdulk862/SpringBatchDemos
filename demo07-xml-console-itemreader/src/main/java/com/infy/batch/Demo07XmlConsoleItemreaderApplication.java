package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo07XmlConsoleItemreaderApplication {

	public static void main(String[] args) {
		
		String[] jobParams = new String[] {"inputFile=InputFile/employees.xml"};
		SpringApplication.run(Demo07XmlConsoleItemreaderApplication.class, jobParams);
	}

}
