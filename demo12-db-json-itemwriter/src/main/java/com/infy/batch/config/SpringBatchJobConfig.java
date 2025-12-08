package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	@Qualifier("customDataSource")
	@Autowired
	private DataSource customDataSource;
	@Autowired
	private JsonFileItemWriter jsonFileItemWriter;
	@Autowired
	private JdbcCursorItemReader<Employee> itemReader;

	@Bean
	public Job employeeJdbcJsonJob() {
		return jobBuilderFactory.get("employeeJdbcJsonJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeJdbcJsonStep())
				.build();
	}
	@Bean
	public Step employeeJdbcJsonStep() {
		return stepBuilderFactory.get("employeeJdbcJsonStep")
				.<Employee,Employee>chunk(3)
				.reader(itemReader)			
				.writer(jsonFileItemWriter)
				.build();				

	}

	

	

	
	
	

}
