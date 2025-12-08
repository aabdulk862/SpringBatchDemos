package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
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
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
				//uncomment the below line to run the same job multiple times with same parameter and auto incremented run id
				 .incrementer(new RunIdIncrementer()) 
				 .start(deciderFirstStep())
				 .next(decider())
				 .from(decider())
				 .on("FN").to(deciderSecondStep())
				 .from(decider())
				 .on("*").to(deciderThirdStep())
				 .end()
				 .build();
	}
	@Bean
	public JobExecutionDecider decider() {
	return new MyFlowDecider();
	}
	@Bean
	public Step deciderFirstStep() {
		return stepBuilderFactory.get("deciderFirstStep")
		.tasklet(firstTask)
		.build();
	}
	//Tasklet is defined inline using lambda expression
	@Bean
	public Step deciderSecondStep() {
		return stepBuilderFactory.get("deciderSecondStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is second tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	@Bean
	public Step deciderThirdStep() {
		return stepBuilderFactory.get("deciderThirdStep")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is third tasklet step");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	
}


