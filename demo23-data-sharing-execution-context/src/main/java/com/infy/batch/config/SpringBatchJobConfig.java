package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
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
	public Job shareJob1() {
		return jobBuilderFactory.get("shareJob1")
				//uncomment the below line to run the same job multiple times with same parameter and auto incremented run id
				 .incrementer(new RunIdIncrementer()) 
				.start(shareFirstStep())
				.on("FAILED").to(shareThirdStep())
				.from(shareFirstStep()).on("*").to(shareSecStep())
				.end()
				.build();
	}
	@Bean
	public Step shareFirstStep() {
		return stepBuilderFactory.get("shareFirstStep")
		.tasklet(firstTask)
		.build();
	}
	//Tasklet is defined inline using lambda expression
	@Bean
	public Step shareSecStep() {
		return stepBuilderFactory.get("shareSecStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is second tasklet step");
			ExecutionContext jobExecutionContext =
					chunkContext.getStepContext()
					.getStepExecution().getJobExecution()
					.getExecutionContext();
					String message = jobExecutionContext.getString(
					"message");
			System.out.println("Messgae from first step : "+message);
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	@Bean
	public Step shareThirdStep() {
		return stepBuilderFactory.get("shareThirdStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is third tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	
}


