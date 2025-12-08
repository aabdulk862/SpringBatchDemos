package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	

	@Bean
	public Job firstJob() {
		return jobBuilderFactory.get("employeeJsonJob")
				.start(employeeJsonStep())
				.build();
	}

	@Bean
	public Step employeeJsonStep() {
		return stepBuilderFactory.get("employeeJsonStep")
				.<Employee,Employee>chunk(3)
				.reader(jsonEmployeeItemReader())			
				.writer(employeeItemWriter())
				.build();				

	}

	@Bean
	public JsonItemReader<Employee> jsonEmployeeItemReader()
	{
		JsonItemReader<Employee> jsonItemReader = new JsonItemReader<>();
		jsonItemReader.setResource(new ClassPathResource("employees.json"));
		jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader<>(Employee.class));
		return jsonItemReader;
	}

	
	@Bean
	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			
			items.stream().forEach(System.out::println);
		};


	}

}
