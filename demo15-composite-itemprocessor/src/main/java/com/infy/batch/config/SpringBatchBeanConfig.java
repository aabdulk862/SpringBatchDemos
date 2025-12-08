
package com.infy.batch.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.infy.batch.bean.Employee;

/*
 * This code uses two data sources, one for storing spring batch metadata tables and another data source for storing 
 * application related tables. Refer application.properties file to have a look into database configuration
 */
@Configuration
public class SpringBatchBeanConfig {
	@Autowired
	private UpperCaseNameItemProcessor upperCaseNameItemProcessor;
	@Autowired
	private NameFilterItemProcessor nameFilterItemProcessor;
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	@Bean(name = "customDataSource")	
	@ConfigurationProperties(prefix = "spring.customdatasource")
	public DataSource customDataSource() {
		return DataSourceBuilder.create().build();
	}

	@StepScope
	@Bean
	public JsonFileItemWriter<Employee> jsonFileItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
		JsonFileItemWriter<Employee> jsonFileItemWriter = 
				new JsonFileItemWriter<>(fileSystemResource, 
						new JacksonJsonObjectMarshaller<Employee>());
		
		return jsonFileItemWriter;
	}
	@Bean
	public JdbcCursorItemReader<Employee> jdbcEmployeeItemReader(){
		JdbcCursorItemReader<Employee> jdbcItemReader = new JdbcCursorItemReader<>();
		jdbcItemReader.setDataSource(customDataSource());
		jdbcItemReader.setSql("select * from employee");
		jdbcItemReader.setRowMapper(new BeanPropertyRowMapper<>() {
			{
				setMappedClass(Employee.class);
			}
		});
		
		return jdbcItemReader;
	}
	@Bean
	public CompositeItemProcessor<Employee, Employee> compositeItemProcessor(){
		CompositeItemProcessor<Employee, Employee> compositeProcessor = new CompositeItemProcessor<>();
		List<ItemProcessor<Employee,Employee>> processors = Arrays.asList(nameFilterItemProcessor,upperCaseNameItemProcessor);
		compositeProcessor.setDelegates(processors);
		return compositeProcessor;
	}
}
