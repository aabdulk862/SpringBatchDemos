package com.infy.batch.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class FirstStepProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("*******StepExecutionListener - afterStep()*******");
		ExecutionContext jobContext = stepExecution.getExecutionContext();
		
       
        jobContext.put("message", "hello");
		return ExitStatus.COMPLETED;
	}

	
	
	

}
