package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	@Autowired
	MySkipListener skipListener;
	@Autowired
	private MyRetryListener retryListener;
	
	@Autowired
	private ItemProcessor<Employee, Employee> itemProcessor;

	@Bean
	public Job skipRetryJob() {
		return jobBuilderFactory.get("skipRetryJob")
				//.incrementer(new RunIdIncrementer())
				//preventRestart()
				.start(skipRetryStep())
				.build();
	}
	//Item processor is written to throw NullPointerException while reading 2nd record. 
	//The below code will retry executing the code twice as retry limit is set to 2 and retry listenr will be called twice
	//After retrying twice, NullPointerException is being thrown, and it is configured for skip, OnSkipInProcess() method of SkipListener will be invoked
	//Comma is missing in 4th record in the given inputfile, which causes FlatFileParseException being thrown while reading.
	//onSkipInRead() mehtod of SkipListener will be invoked
	@Bean
	public Step skipRetryStep() {
		return stepBuilderFactory.get("skipRetryStep")							
				.<Employee,Employee>chunk(3)				
				.reader(employeeItemReader())					
				.processor(itemProcessor)				
				.writer(employeeItemWriter())				
				.faultTolerant()
				.skipLimit(2)
				//.skipLimit(Integer.MAX_VALUE)
				.noSkip(Exception.class)	
				.skip(FlatFileParseException.class)
				.skip(NullPointerException.class)
				//if not specifying limit, we can configure always skip policy
				//.skipPolicy(new AlwaysSkipItemSkipPolicy())
				
				.retryLimit(2)
				.retry(NullPointerException.class)
			
				.listener(retryListener)
				.listener(skipListener)
				.build();				

	}

	@Bean
	public FlatFileItemReader<Employee> employeeItemReader() { 
		return new FlatFileItemReaderBuilder<Employee>()
			.name("employeeItemReader")
			.resource(new ClassPathResource("employees.csv"))
			.linesToSkip(1)
			.delimited()
			.names("empId","empName","email","jobLoc")
			.targetType(Employee.class)
			.build(); 
	}


	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			System.out.println("inside item writer");
			items.stream().forEach(System.out::println);
		};


	}

}
