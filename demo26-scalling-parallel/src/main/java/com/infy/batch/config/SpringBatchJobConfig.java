package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.infy.batch.bean.Claim;
import com.infy.batch.bean.Employee;

@Configuration
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	
	@Qualifier("customDataSource")
	@Autowired
	private DataSource customDataSource;
	@Autowired
	private JdbcBatchItemWriter<Employee> empJdbcItemWriter;
	@Autowired
	private FlatFileItemReader<Employee> empFileItemReader;
	@Autowired
	private JdbcBatchItemWriter<Claim> claimJdbcItemWriter;
	@Autowired
	private FlatFileItemReader<Claim> claimFileItemReader;
	@Autowired
	private EmployeeItemProcessor employeeItemProcessor;
	@Autowired
	private ClaimItemProcessor claimItemProcessor;
	@Bean
	public Job employeeParallelJob() {
		return jobBuilderFactory.get("employeeParallelJob")
				.incrementer(new RunIdIncrementer())
				.start(parallelFlows())
				.end()
				.build();
	}
	@Bean
	public Step employeeStep1() {
		return stepBuilderFactory.get("employeeStep1")
				.<Employee,Employee>chunk(5)
				.reader(empFileItemReader)	
				.processor(employeeItemProcessor)
				.writer(empJdbcItemWriter)				
				.build();				

	}
	

	@Bean
	public Step claimStep2() {
		return stepBuilderFactory.get("claimStep2")
				.<Claim,Claim>chunk(5)
				.reader(claimFileItemReader)
				.processor(claimItemProcessor)
				.writer(claimJdbcItemWriter)				
				.build();				

	}
	
	

	@Bean
	public Flow flow2() {
	    return new FlowBuilder<SimpleFlow>("flow2")
	            .start(claimStep2())
	            .build();
	}
	
	@Bean
	public Flow parallelFlows() {
	    return new FlowBuilder<SimpleFlow>("parallelFlows")
	            .start(employeeStep1())
	            .split(new SimpleAsyncTaskExecutor())
	            .add(flow2())
	            .build();
	            
	}

	
	

	

	
	
	

}
