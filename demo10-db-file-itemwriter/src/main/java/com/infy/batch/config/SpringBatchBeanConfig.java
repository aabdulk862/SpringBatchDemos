package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
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
	public FlatFileItemWriter<Employee> flatFileItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
//		FlatFileItemWriter<Employee> flatFileItemWriter = 
//				new FlatFileItemWriter<Employee>();
//
//		flatFileItemWriter.setResource(fileSystemResource);
//
//		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
//			@Override
//			public void writeHeader(Writer writer) throws IOException {
//				writer.write("EmpId,EmpName,Email,JobLoc");
//			}
//		});
//
//		flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Employee>() {
//			{
//				//setDelimiter("|");
//				setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {
//					{
//						setNames(new String[] {"empId", "empName", "email", "jobLoc"});
//					}
//				});
//			}
//		});
//
//		flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
//			@Override
//			public void writeFooter(Writer writer) throws IOException {
//				writer.write("Created @ " + new Date());
//			}
//		});
//
//		return flatFileItemWriter;

		//Creation of FlatFileItemWriter object can be simplified as given below without header and footer

		return new FlatFileItemWriterBuilder<Employee>()
					.name("employeeItemWriter")
					.resource(fileSystemResource)
					.delimited()
					.delimiter(";")
					.names(new String[] {"empId", "empName", "email", "jobLoc"})
					.build();
		 
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
	
}
