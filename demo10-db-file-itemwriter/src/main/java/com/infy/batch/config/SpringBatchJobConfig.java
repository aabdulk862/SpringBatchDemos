package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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
	private FlatFileItemWriter<Employee> flatFileItemWriter;
	
	@Autowired
	private JdbcCursorItemReader<Employee> jdbcCursorItemReader;

	@Bean
	public Job employeeJdbcFileJob() {
		return jobBuilderFactory.get("employeeJdbcFileJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeJdbcFileStep())
				.build();
	}
	@Bean
	public Step employeeJdbcFileStep() {
		return stepBuilderFactory.get("employeeJdbcFileStep")
				.<Employee,Employee>chunk(3)
				.reader(jdbcCursorItemReader)			
				.writer(flatFileItemWriter)
				.build();				

	}

	


}
