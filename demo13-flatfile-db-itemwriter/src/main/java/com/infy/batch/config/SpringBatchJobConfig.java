package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	private JdbcBatchItemWriter<Employee> jdbcBatchItemWriter;
	@Autowired
	private FlatFileItemReader<Employee> flatFileItemReader;

	@Bean
	public Job employeeCsvDbJob() {
		return jobBuilderFactory.get("employeeCsvDbJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeCsvDBStep())
				.build();
	}
	@Bean
	public Step employeeCsvDBStep() {
		return stepBuilderFactory.get("employeeCsvDbStep")
				.<Employee,Employee>chunk(3)
				.reader(flatFileItemReader)			
				.writer(jdbcBatchItemWriter)
				.build();				

	}

	
	

	

	
	
	

}
