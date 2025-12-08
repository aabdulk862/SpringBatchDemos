package com.infy.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class MyJobListener  {
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
       System.out.println("**********JobExecutionListener - beforeJob()**********");
    }
	@AfterJob
    public void afterJob(JobExecution jobExecution) {
    	System.out.println("*********JobExecutionListener - Called afterJob()*********");
    }
}
