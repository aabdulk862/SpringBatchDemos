package com.infy.batch.config;

import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

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
	@StepScope
	public FlatFileItemReader<Employee> flatFileEmpItemReader(@Value("#{stepExecutionContext['file']}") Resource resource) throws MalformedURLException {
		FlatFileItemReader<Employee> flatFileItemReader = 
				new FlatFileItemReader<Employee>();

		flatFileItemReader.setResource(resource);

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
		flatFileItemReader.setSaveState(false);

		return flatFileItemReader;
	}
	
	




//	
	
	@Bean
	public JdbcBatchItemWriter<Employee> jdbcBatchEmpItemWriter() {
		return new JdbcBatchItemWriterBuilder<Employee>()
				.dataSource(customDataSource())
				.sql("insert into employee(empid,empname,email,jobloc) values (:empId,:empName,:email,:jobLoc)")
				.beanMapped()
				.build();
	}
	

	@Bean
	
	public MultiResourcePartitioner partitioner() throws Exception{
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("/*.csv");
		partitioner.setKeyName("file");
		partitioner.setResources(resources);
		
		return partitioner;
	}

	

}
