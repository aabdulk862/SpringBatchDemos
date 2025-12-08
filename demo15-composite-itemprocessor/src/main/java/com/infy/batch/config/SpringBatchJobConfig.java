package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	
	@Autowired
	private JdbcCursorItemReader<Employee> jdbcEmployeeItemReader;
	@Autowired
	private JsonFileItemWriter jsonFileItemWriter;
	@Autowired
	private  CompositeItemProcessor<Employee, Employee>  compositeItemProcessor; 

	@Bean
	public Job employeeSqlJsonCPJob() {
		return jobBuilderFactory.get("employeeSqlJsonCPJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeSqlJsonCPStep())
				.build();
	}
	@Bean
	public Step employeeSqlJsonCPStep() {
		return stepBuilderFactory.get("employeeSqlJsonCPStep")
				.<Employee,Employee>chunk(3)
				.reader(jdbcEmployeeItemReader)	
				.processor(compositeItemProcessor)
				.writer(jsonFileItemWriter)
				.build();				

	}
	
	
	
	

	

	
	
	

}
