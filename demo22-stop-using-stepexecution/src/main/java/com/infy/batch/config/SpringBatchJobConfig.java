package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infy.batch.listener.MyStepListener;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	

	/* In the below job, Step Listener of Step1 is controlling
	 * the execution of Job by setting the exit status programmatically
	 */
	
	@Bean
	public Job stopStepExecutionJob() {
		return jobBuilderFactory.get("stopStepExecutionJob")				
				.start(employeeStep1())
				.next(employeeStep2())
				
				.build();
	}
	@Bean
	public Step employeeStep1() {
		return stepBuilderFactory.get("employeeStep1")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 1");
			return RepeatStatus.FINISHED;		
		})
		.listener(new MyStepListener())
		.build();
	}
	
	@Bean
	public Step employeeStep2() {
		return stepBuilderFactory.get("employeeStep2")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 2");			
			return RepeatStatus.FINISHED;		
		})
		.build();
	}

	

}
