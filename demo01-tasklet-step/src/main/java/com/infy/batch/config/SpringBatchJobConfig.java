package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	@Autowired
	private FirstTask firstTask;
	
	@Bean
	public Job firstJob() {
		return jobBuilderFactory.get("firstJob")
		.incrementer(new RunIdIncrementer())
		.start(firstStep())
		.build();
	}
	@Bean
	public Step firstStep() {
		return stepBuilderFactory.get("firstStep")
		.tasklet(firstTask)
		.build();
	}
}


