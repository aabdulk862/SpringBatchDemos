package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
	public Job conditionalFlowJob() {
		return jobBuilderFactory.get("conditionalFlowJob")
				//uncomment the below line to run the same job multiple times with same parameter and auto incremented run id
				 .incrementer(new RunIdIncrementer()) 
				.start(flowFirstStep())
				.on("FAILED").to(flowThirdStep())
				.from(flowFirstStep()).on("*").to(flowSecStep())
				.end()
				.build();
	}
	@Bean
	public Step flowFirstStep() {
		return stepBuilderFactory.get("flowFirstStep")
		.tasklet(firstTask)
		.build();
	}
	
	@Bean
	public Step flowSecStep() {
		return stepBuilderFactory.get("flowSecStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is second tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	@Bean
	public Step flowThirdStep() {
		return stepBuilderFactory.get("flowThirdStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is third tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	
}


