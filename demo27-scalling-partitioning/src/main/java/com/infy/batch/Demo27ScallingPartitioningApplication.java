package com.infy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo27ScallingPartitioningApplication {

	public static void main(String[] args) {

		//String[] jobParams = new String[] {"inputFiles=InputFile/employees*.csv"};
		SpringApplication.run(Demo27ScallingPartitioningApplication.class, args);
	}

}
