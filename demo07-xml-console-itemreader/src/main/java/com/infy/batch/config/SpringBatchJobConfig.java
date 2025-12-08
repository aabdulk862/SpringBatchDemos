package com.infy.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.infy.batch.bean.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;	

	@Bean
	public Job employeeXmlJob() {
		return jobBuilderFactory.get("employeeXmlJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeXmlStep())
				.build();
	}
	
	@Bean
	public Step employeeXmlStep() {
		return stepBuilderFactory.get("employeeXmlStep")
				.<Employee,Employee>chunk(3)
				.reader(staxEventEmployeeItemReader(null))			
				.writer(employeeItemWriter())
				.build();				

	}


	@Bean
	@StepScope
	public StaxEventItemReader<Employee> staxEventEmployeeItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
		//		StaxEventItemReader<Employee> staxEventItemReader = 
		//				new StaxEventItemReader<>();
		//		
		//		staxEventItemReader.setResource(fileSystemResource);
		//		staxEventItemReader.setFragmentRootElementName("employee");
		//		staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller() {
		//			{
		//				setClassesToBeBound(Employee.class);
		//			}
		//		});
		//		
		//		return staxEventItemReader;
		return new StaxEventItemReaderBuilder<Employee>()
				.name("empXmlReader")
				.resource(fileSystemResource)
				.addFragmentRootElements("employee")
				.unmarshaller(new Jaxb2Marshaller() {
					{
						setClassesToBeBound(Employee.class);
					}
				})
				.build();
	}


	@Bean
	public ItemWriter<Employee> employeeItemWriter() {
		return items->{
			System.out.println("inside item writer");
			items.stream().forEach(System.out::println);
		};


	}

}
