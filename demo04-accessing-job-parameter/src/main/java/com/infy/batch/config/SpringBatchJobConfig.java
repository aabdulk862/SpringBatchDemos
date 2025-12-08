package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
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
		return jobBuilderFactory.get("paramJob3")
				//uncomment the below line to run the same job multiple times with same parameter and auto incremented run id
				/* .incrementer(new RunIdIncrementer()) */
		.start(paramStep())
		
		.build();
	}
	@Bean
	public Step paramStep() {
		return stepBuilderFactory.get("paramStep")
		.tasklet(firstTask)
		.build();
	}

	
}


