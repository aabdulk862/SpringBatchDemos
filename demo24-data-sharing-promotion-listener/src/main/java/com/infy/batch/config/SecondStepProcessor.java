package com.infy.batch.config;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class SecondStepProcessor implements ItemProcessor<Employee, Employee>{
	private String message;
	
	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		
		item.setEmpName(item.getEmpName()+message);
		return item;
	}
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		
		
		ExecutionContext jobContext = stepExecution.getJobExecution()
                .getExecutionContext();
       
        message =  (String) jobContext.get("message");
        System.out.println("message "+message);
		
	}


}
