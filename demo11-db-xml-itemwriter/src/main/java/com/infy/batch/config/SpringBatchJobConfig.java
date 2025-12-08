package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
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
	private StaxEventItemWriter staxEventItemWriter;

	@Bean
	public Job employeeJdbcXmlJob() {
		return jobBuilderFactory.get("employeeJdbcXmlJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeJdbcXmlStep())
				.build();
	}
	@Bean
	public Step employeeJdbcXmlStep() {
		return stepBuilderFactory.get("employeeJdbcXmlStep")
				.<Employee,Employee>chunk(3)
				.reader(jdbcEmployeeItemReader())			
				.writer(staxEventItemWriter)
				.build();				

	}

	@Bean
	public JdbcCursorItemReader<Employee> jdbcEmployeeItemReader(){
		JdbcCursorItemReader<Employee> jdbcItemReader = new JdbcCursorItemReader<>();
		jdbcItemReader.setDataSource(customDataSource);
		jdbcItemReader.setSql("select * from employee");
		jdbcItemReader.setRowMapper(new BeanPropertyRowMapper<>() {
			{
				setMappedClass(Employee.class);
			}
		});		
		return jdbcItemReader;
	}

	

}
