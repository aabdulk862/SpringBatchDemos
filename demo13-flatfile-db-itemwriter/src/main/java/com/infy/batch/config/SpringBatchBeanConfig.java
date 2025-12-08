package com.infy.batch.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;

import com.infy.batch.bean.Employee;

/*
 * This code uses two data sources, one for storing spring batch metadata tables and another data source for storing 
 * application related tables. Refer application.properties file to have a look into database configuration
 */
@Configuration
public class SpringBatchBeanConfig {
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
	public FlatFileItemReader<Employee> flatFileItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemReader<Employee> flatFileItemReader = 
				new FlatFileItemReader<Employee>();
		
		flatFileItemReader.setResource(fileSystemResource);
		
		flatFileItemReader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("EMPID", "EMPNAME", "EMAIL", "JOBLOC");
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});
				
			}
			
		});
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	
//	@Bean
//	public JdbcBatchItemWriter<Employee> jdbcBatchItemWriter() {
//		JdbcBatchItemWriter<Employee> jdbcBatchItemWriter = 
//				new JdbcBatchItemWriter<Employee>();
//		
//		jdbcBatchItemWriter.setDataSource(customDataSource());
//		jdbcBatchItemWriter.setSql(
//				"insert into employee(empid, empname, email, jobloc) "
//				+ "values (?,?,?,?)");
//		
//		jdbcBatchItemWriter.setItemPreparedStatementSetter(
//				new ItemPreparedStatementSetter<Employee>() {
//			
//			@Override
//			public void setValues(Employee item, PreparedStatement ps) throws SQLException {
//				ps.setLong(1, item.getEmpId());
//				ps.setString(2, item.getEmpName());
//				ps.setString(3, item.getEmail());
//				ps.setString(4, item.getJobLoc());
//			}
//		});
//		
//		return jdbcBatchItemWriter;
//	}
	@Bean
	public JdbcBatchItemWriter<Employee> jdbcBatchItemWriter() {
		return new JdbcBatchItemWriterBuilder<Employee>()
				.dataSource(customDataSource())
				.sql("insert into employee(empid,empname,email,jobloc) values (:empId,:empName,:email,:jobLoc)")
				.beanMapped()
				.build();
	}
}
