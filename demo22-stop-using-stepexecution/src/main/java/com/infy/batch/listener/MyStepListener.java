package com.infy.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class MyStepListener {
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		boolean shouldRun = shouldJobRun();
	    if (!shouldRun) {
	        // listeners will still work, but any other step logic (reader, processor, writer) will not happen
	        stepExecution.setTerminateOnly();
	        stepExecution.setExitStatus(new ExitStatus("STOPPED", "Job should not be run right now."));
	        System.out.println("*******StepExecutionListener - beforeStep()*******");
	    }
	        
	}
 
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("*******StepExecutionListener - afterStep()*******");
		return ExitStatus.COMPLETED;
	}
	public boolean shouldJobRun() {
		return false;
	}

}
