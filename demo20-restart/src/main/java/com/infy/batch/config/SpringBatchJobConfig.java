package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
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
	private ItemProcessor<Employee, Employee> itemProcessor;

	//In this job ItemWriter3 of Step3 is configured to throw exception 
	//On 2nd execution 2nd step will run even though it is completed during 1st execution
	//On 3rd excetuion step 2 will run again and step 3 will not run as maximum start limit is set to 2
	@Bean
	public Job restartJob() {
		return jobBuilderFactory.get("restartJob")
				//.incrementer(new RunIdIncrementer())
				//preventRestart() will prevent the execution of this job even if it is failed
				//.preventRestart()
				.start(restartStep1())
				.next(restartStep2())
				.next(restartStep3())
				.build();
	}
	@Bean
	public Step restartStep1() {
		return stepBuilderFactory.get("restartStep1")							
				.<Employee,Employee>chunk(3)				
				.reader(employeeItemReader1())					
				.processor(itemProcessor)				
				.writer(employeeItemWriter1())				
				.build();				

	}
	@Bean
	public Step restartStep2() {
		return stepBuilderFactory.get("restartStep2")							
				.<Employee,Employee>chunk(3)				
				.reader(employeeItemReader2())					
				.processor(itemProcessor)				
				.writer(employeeItemWriter2())	
				//Setting allowStartIfComplete() to true allows this step to get restarted even if it completed in previous execution
				.allowStartIfComplete(true)			
				.build();				

	}
	@Bean
	public Step restartStep3() {
		return stepBuilderFactory.get("restartStep3")							
				.<Employee,Employee>chunk(3)				
				.reader(employeeItemReader3())					
				.processor(itemProcessor)				
				.writer(employeeItemWriter3())	
				//Below startlimit configuration allows the step to be executed only twice	
				.startLimit(2)			
				.build();				

	}

	@Bean

	public FlatFileItemReader<Employee> employeeItemReader1() { 
		return new FlatFileItemReaderBuilder<Employee>()
			.name("employeeItemReader1")
			.resource(new ClassPathResource("employees.csv"))
			.linesToSkip(1)
			.lineMapper(lineMapper())			
			.build(); 
	}
	@Bean
	public ItemWriter<Employee> employeeItemWriter1() {
		return items->{
			System.out.println("inside item writer1");
			items.stream().forEach(System.out::println);
		};


	}
	@Bean
	public FlatFileItemReader<Employee> employeeItemReader2() { 
		return new FlatFileItemReaderBuilder<Employee>()
			.name("employeeItemReader2")
			.resource(new ClassPathResource("employees.csv"))
			.linesToSkip(1)
			.lineMapper(lineMapper())			
			.build(); 
	}
	@Bean
	public ItemWriter<Employee> employeeItemWriter2() {
		return items->{
			System.out.println("inside item writer2");
			items.stream().forEach(System.out::println);
		};


	}
	@Bean
	public FlatFileItemReader<Employee> employeeItemReader3() { 
		return new FlatFileItemReaderBuilder<Employee>()
			.name("employeeItemReader3")
			.resource(new ClassPathResource("employees.csv"))
			.linesToSkip(1)
			.lineMapper(lineMapper())			
			.build(); 
	}
	@Bean
	public ItemWriter<Employee> employeeItemWriter3() {
		return items->{
			System.out.println("inside item writer3");			
			//items.stream().forEach(System.out::println);
			throw new Exception("Item Writer 3 throwing exception");
		};


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


}
