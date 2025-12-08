package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	


	@Autowired 
	private JsonFileItemWriter<Employee> jsonFileItemWriter;

	@Autowired
	private FirstStepProcessor firstStepProcessor;
	@Autowired
	private SecondStepProcessor secondStepProcessor;
	
	@Autowired
	private FlatFileItemReader<Employee> itemReader;
	
	@Autowired
	@Qualifier("itemWriter")
	private ItemWriter<Employee> itemWriter;
	
	@Autowired
	private ExecutionContextPromotionListener promotionListener;

	@Bean
	public Job chunkShareJob() {
		return jobBuilderFactory.get("chunkShareJob")
				.incrementer(new RunIdIncrementer())
				.start(chunkShareStep1())
				.next(chunkShareStep2())
				.build();
	}

	public Step chunkShareStep1() {
		return stepBuilderFactory.get("chunkShareStep1")
				.<Employee,Employee>chunk(3)
				.reader(itemReader)
				.processor(firstStepProcessor)
				.writer(itemWriter)
				.listener(promotionListener)
				.build();				

	}
	public Step chunkShareStep2() {
		return stepBuilderFactory.get("chunkShareStep2")
				.<Employee,Employee>chunk(3)
				.reader(itemReader)	
				.processor(secondStepProcessor)
				.writer(jsonFileItemWriter)
				.build();				

	}




	
}
