package com.infy.batch.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.infy.batch.bean.Claim;
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

	
	@Bean	
	public FlatFileItemReader<Employee> flatFileEmpItemReader(){
		return new FlatFileItemReaderBuilder<Employee>()
				.name("flatfileReader")
				.resource(new ClassPathResource("employees.csv"))
				.linesToSkip(1)
				.delimited()
				.delimiter(",")
				.names(new String[] {"EMPID", "EMPNAME", "EMAIL", "JOBLOC"})
				.targetType(Employee.class)
				.saveState(false)
				.build();

	}
	

	
	@Bean	
	public FlatFileItemReader<Claim> flatFileClaimItemReader(){
		return new FlatFileItemReaderBuilder<Claim>()
				.name("flatfileReader")
				.resource(new ClassPathResource("claims.csv"))
				.linesToSkip(1)
				.delimited()
				.delimiter(",")
				.names(new String[] {"CLAIMID", "CLAIMNAME", "REQUESTEDBY", "CLAIMAMOUNT"})
				.targetType(Claim.class)
				.saveState(false)
				.build();

	}
	
		
	@Bean
	public JdbcBatchItemWriter<Employee> jdbcBatchEmpItemWriter() {
		return new JdbcBatchItemWriterBuilder<Employee>()
				.dataSource(customDataSource())
				.sql("insert into employee(empid,empname,email,jobloc) values (:empId,:empName,:email,:jobLoc)")
				.beanMapped()
				.build();
	}
	

	@Bean
	public JdbcBatchItemWriter<Claim> jdbcBatchClaimItemWriter() {
		return new JdbcBatchItemWriterBuilder<Claim>()
				.dataSource(customDataSource())
				.sql("insert into claim(claimid, claimname, requestedby, amount) values (:claimid,:claimname,:requestedby,:amount)")
				.beanMapped()
				.build();
	}
}
