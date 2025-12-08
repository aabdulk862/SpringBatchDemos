package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.infy.batch.bean.Employee;

@Configuration

public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	

	@Qualifier("customDataSource")
	@Autowired
	private DataSource customDataSource;
	@Autowired
	private EmployeeItemProcessor employeeItemProcessor;


	@Bean
	public Job employeeThreadJob() {
		return jobBuilderFactory.get("employeeThreadJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeThreadStep())

				.build();
	}
	@Bean
	public Step employeeThreadStep() {
		return stepBuilderFactory.get("employeeThreadStep")
				.<Employee,Employee>chunk(50)
				.reader(flatFileItemReader(null))	
				.processor(employeeItemProcessor)
				.writer(jdbcBatchItemWriter())
				.taskExecutor(taskExecutor())
				.build();				

	}
	@Bean
	public TaskExecutor taskExecutor() {
		//	SimpleAsyncTaskExceutor will not resue threads. ThreadPoolExecutor provides pooling option
//		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
//		executor.setConcurrencyLimit(5);


		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(10);
		
		executor.setThreadNamePrefix("mythread");		
		return executor;
	}


	@Bean
	@StepScope
	public FlatFileItemReader<Employee> flatFileItemReader(@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource){
		return new FlatFileItemReaderBuilder<Employee>()
				.name("flatfileReader")
				.resource(fileSystemResource)
				.linesToSkip(1)
				.delimited()
				.delimiter(",")
				.names(new String[] {"empId","empName","email","jobLoc"})
				.targetType(Employee.class)
				.saveState(false)
				.build();

	}
	@Bean
	public JdbcBatchItemWriter<Employee> jdbcBatchItemWriter() {
		return new JdbcBatchItemWriterBuilder<Employee>()
				.dataSource(customDataSource)
				.sql("insert into employee(empid,empname,email,jobloc) values (:empId,:empName,:email,:jobLoc)")
				.beanMapped()
				.build();
	}










}
