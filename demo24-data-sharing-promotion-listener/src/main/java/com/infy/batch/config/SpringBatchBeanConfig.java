package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.infy.batch.bean.Employee;


@Configuration
public class SpringBatchBeanConfig {	


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
	public FlatFileItemReader<Employee> employeeItemReader() { 
		return new FlatFileItemReaderBuilder<Employee>()
				.name("employeeItemReader")
				.resource(new ClassPathResource("employees.csv"))
				.linesToSkip(1)
				.lineMapper(lineMapper())			
				.build(); 
	}

	@Bean
	public LineMapper<Employee> lineMapper()
	{
		final DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		//setting the delimitter is optional as comma will be taken as the default delimiter
		//delimitedLineTokenizer.setDelimiter(",");	      
		delimitedLineTokenizer.setNames("EMPID","EMPNAME","EMAIL","JOBLOC");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>(){{
			setTargetType(Employee.class);
		}});

		return defaultLineMapper;
	}

	@Bean("itemWriter")
	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			System.out.println("inside item writer");
			items.stream().forEach(System.out::println);
		};


	}
	@Bean
	public ExecutionContextPromotionListener promotionListener() {
	    ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
	    listener.setKeys(new String[] {"message" });
	    return listener;
	}

}
