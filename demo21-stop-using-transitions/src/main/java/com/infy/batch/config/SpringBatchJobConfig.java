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

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	
	

	@Bean
	public Job stopTransitionsJob() {
		return jobBuilderFactory.get("stopTransitionsJob")				
				.start(employeeStep1())
				.next(employeeStep2())
				/* step 2 is defined to throw exception at run time and below 
				 * code runs step 4 next and marks the job status as completed 
				 */
				.on("FAILED").to(employeeStep4())
				/*comment the above line and uncomment the below so that on failed status of step 2, we can pause the job, 
				with job status as "STOPPED", fix the step manually and rerun the job later*/
				//.on("FAILED").stop()
				/*Another possibility of state transition occurrence can be with fail(), causing the job to fail 
				comment the above line and uncomment the below*/
				//.on("FAILED").fail()
				.from(employeeStep2())
				.on("*").to(employeeStep3())
				.end()
				.build();
	}
	@Bean
	public Step employeeStep1() {
		return stepBuilderFactory.get("employeeStep1")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 1");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	@Bean
	public Step employeeStep2() {
		return stepBuilderFactory.get("employeeStep2")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 2");
			throw new RuntimeException("exception from step 2");
			//return RepeatStatus.FINISHED;		
		})
		.build();
	}
	@Bean
	public Step employeeStep3() {
		return stepBuilderFactory.get("employeeStep3")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 3");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	@Bean
	public Step employeeStep4() {
		return stepBuilderFactory.get("employeeStep4")
		.tasklet(( contribution,  chunkContext)->{
			System.out.println("This is Employee Step 4");
			return RepeatStatus.FINISHED;		
		})
		.build();
	}
	
	
	
	
	

	
	

	

	
	
	

}
