package com.infy.batch.config;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MyFlowDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("current time : " +LocalDateTime.now().getHour());
		String result=LocalDateTime.now().getHour()<12?"FN":"AN";
		return new FlowExecutionStatus(result);
	}

}
