package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.infy.batch.bean.Employee;
import com.infy.batch.listener.MyChunkListener;
import com.infy.batch.listener.MyItemProcessListener;
import com.infy.batch.listener.MyItemReadListener;
import com.infy.batch.listener.MyItemWriteListener;
import com.infy.batch.listener.MyJobListener;
import com.infy.batch.listener.MySkipListener;
import com.infy.batch.listener.MyStepListener;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	
	
	@Autowired
	private ItemProcessor<Employee, Employee> itemProcessor;

	@Bean
	public Job listenerJob() {
		return jobBuilderFactory.get("ListenerJob")
				.incrementer(new RunIdIncrementer())
				.listener(new MyJobListener())				
				.start(empListenerStep())
				.build();
	}
	//uncomment the below listener configuration one by one to understand the execution flow
	
	@Bean
	public Step empListenerStep() {
		return stepBuilderFactory.get("empListenerStep")
				.listener(new MyStepListener())				
				.<Employee,Employee>chunk(3)
				.listener(new MyChunkListener())
				.reader(employeeItemReader())	
				.listener(new MyItemReadListener())
				.processor(itemProcessor)
				//.listener(new MyItemProcessListener())
				.writer(employeeItemWriter())
				//.listener(new MyItemWriteListener())
				//.faultTolerant()
				//.skipLimit(2)
				//.skip(Exception.class)
				//.listener(new MySkipListener())
				.build();				

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
	
   @Bean
	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			System.out.println("inside item writer");
			items.stream().forEach(System.out::println);
		};


	}

}
