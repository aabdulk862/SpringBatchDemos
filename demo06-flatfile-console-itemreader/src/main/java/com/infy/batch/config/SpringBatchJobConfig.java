package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
		return jobBuilderFactory.get("employeeChunkJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeChunkStep())
				.build();
	}
	@Bean
	public Step employeeChunkStep() {
		return stepBuilderFactory.get("employeeChunkStep")
				.<Employee,Employee>chunk(3)
				//.reader(employeeItemReader())
				//comment the above line and uncomment the blow for using FileSystemResource
				.reader(employeeItemReaderUsingParam(null)) 
				.writer(employeeItemWriter())
				.build();				

	}
//	@Bean
//	public FlatFileItemReader<Employee> employeeItemReader() { 
//		return new FlatFileItemReaderBuilder<Employee>()
//			.name("employeeItemReader")
//			.resource(new ClassPathResource("employees.csv"))
//			.linesToSkip(1)
//			.delimited()
//			.names("empId","empName","email","jobLoc")
//			.targetType(Employee.class)
//			.build(); 
//	}

	

	//comment the above employeeItemReader() method and uncomment the below to know 
	//how to use FileSystemResource to get file name from parameter
	//Before execution, go to RunAs->RunConfiguraitons->Arguments->Program Argument and provide value for job parameter as 
	//"inputFile=<your workspace location>\demo06-flatfile-console-itemreader\InputFile\employees.csv

	@Bean
	@StepScope
	public FlatFileItemReader<Employee> employeeItemReaderUsingParam(@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource)
  
	{
		return new FlatFileItemReaderBuilder<Employee>()
				.name("employeeItemReader")
				.resource(fileSystemResource)	                
				.linesToSkip(1)
				.delimited()
				.names("empId","empName","email","jobLoc")
				.targetType(Employee.class)				
				.build();
	}
	
	
	@Bean
	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			System.out.println("inside item writer");
			items.stream().forEach(System.out::println);
		};


	}
	
	

	//Below itemReader uses explicit LineMapper configuration to map the csv record with Employee object

//	@Bean
//	public FlatFileItemReader<Employee> employeeItemReader() { 
//		return new FlatFileItemReaderBuilder<Employee>()
//			.name("employeeItemReader")
//			.resource(new ClassPathResource("employees.csv"))
//			.linesToSkip(1)
//			.lineMapper(lineMapper())			
//			.build(); 
//	}
	

//	@Bean
//	public LineMapper<Employee> lineMapper()
//	{
//		final DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
//		final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
//		//setting the delimitter is optional as comma will be taken as the default delimiter
//		//delimitedLineTokenizer.setDelimiter(",");	      
//		delimitedLineTokenizer.setNames("EMPID","EMPNAME","EMAIL","JOBLOC");
//		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
//		defaultLineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>(){{
//			setTargetType(Employee.class);
//		}});
//
//		return defaultLineMapper;
//	}
	
	
	
}
