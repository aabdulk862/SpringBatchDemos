package com.infy.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Demo05BuildingJobParameterApplication implements CommandLineRunner {

	@Autowired
	@Qualifier(value = "jobParamJob")
	private Job jobParamJob;
	
	@Autowired
	private JobLauncher jobLauncher;
	

	public static void main(String[] args) {
		SpringApplication.run(Demo05BuildingJobParameterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		JobParameters jobParameters = new JobParametersBuilder()
				.addDate("schduleDate", new java.util.Date())
				.addString("name", "Alex").toJobParameters();


		jobLauncher.run(jobParamJob, jobParameters);
		
	}

}
