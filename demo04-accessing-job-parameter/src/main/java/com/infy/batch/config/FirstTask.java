package com.infy.batch.config;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@StepScope
//Job parameters can be accessed either through ChunkContext or through dependency injection via @Value annotation
//@StepScope annotation is must to access the parameter so that creation of FirstTask bean is deferred till step exectuion

public class FirstTask implements Tasklet{
	@Value("#{jobParameters['name']}") 
	String name;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		/*
		 * String name = (String) chunkContext.getStepContext() .getJobParameters()
		 * .get("name");
		 */
				System.out.println("Hello,"+ name + "!");
		return RepeatStatus.FINISHED;
	}

}

