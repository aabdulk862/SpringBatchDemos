package com.infy.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	

	@Qualifier("customDataSource")
	@Autowired
	private DataSource customDataSource;
	@Autowired
	private JdbcBatchItemWriter<Employee> empJdbcItemWriter;
	@Autowired
	private FlatFileItemReader<Employee> empFileItemReader;
	@Autowired
	private EmployeeItemProcessor employeeItemProcessor;
	@Autowired
	private Partitioner partitioner;

	

	@Bean
	public Job partitionedJob() {
		return this.jobBuilderFactory.get("partitionedJob")
				.incrementer(new RunIdIncrementer())
				.start(partitionedMaster())
				.build();
	}



	@Bean
	public Step partitionedMaster() {
		return this.stepBuilderFactory.get("masterStep")
				.partitioner(partitionedSlave().getName(), partitioner)
				.partitionHandler(partitionHandler())
				.build();
	}
	
	@Bean
	public Step partitionedSlave() {
		return stepBuilderFactory.get("SlaveStep")
				.<Employee, Employee>chunk(3)
				.reader(empFileItemReader)
				.writer(empJdbcItemWriter)
				.build();
	}
	@Bean
	public TaskExecutorPartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler partitionHandler =
				new TaskExecutorPartitionHandler();
		partitionHandler.setStep(partitionedSlave());
		partitionHandler.setGridSize(2);
		partitionHandler.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return partitionHandler;
	}
	






}
