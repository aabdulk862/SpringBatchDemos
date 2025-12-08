package com.infy.batch.config;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

@Service
public class FirstTask implements Tasklet{

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("This is first tasklet step");
		ExecutionContext jobExecutionContext =
				chunkContext.getStepContext()
				.getStepExecution().getJobExecution()
				.getExecutionContext();
		jobExecutionContext.putString("message", "hello");		
		return RepeatStatus.FINISHED;
		//throw new RuntimeException("An error occured in first step");
	}

}

