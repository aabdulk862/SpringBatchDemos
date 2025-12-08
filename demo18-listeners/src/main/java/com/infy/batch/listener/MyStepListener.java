package com.infy.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class MyStepListener {
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("*******StepExecutionListener - beforeStep()********");		
	}
 
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("*******StepExecutionListener - afterStep()*******");
		return ExitStatus.COMPLETED;
	}

}
