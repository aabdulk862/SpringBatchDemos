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
	
	//multiple steps are executed sequentially one after other using next() method
	@Bean
	public Job multiStepJob() {
		return jobBuilderFactory.get("MultiStepJob")
				
		.start(multiFirstStep())
		.next(multiSecStep())
		.build();
	}
	
	@Bean
	public Step multiFirstStep() {
		return stepBuilderFactory.get("multiFirstStep")
		.tasklet(firstTask)
		.build();
	}
	//Tasklet is defined inline using lambda expression
	@Bean
	public Step multiSecStep() {
		return stepBuilderFactory.get("multiSecStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is second tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	
}


