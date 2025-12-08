package com.infy.batch.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;
@Component
public class MySkipListener {
	private static final Log logger = LogFactory.getLog(MySkipListener.class);
	 @OnSkipInRead
	    public void onSkipInRead(Throwable t) {
		 if(t instanceof FlatFileParseException) {
			 FlatFileParseException ffpe = (FlatFileParseException) t;
			 StringBuilder errorMessage = new StringBuilder();
			 errorMessage.append("An error occured while reading the " +
			 ffpe.getLineNumber() +
			 " line of the file. Below was the faulty " +
			 "input.\n");
			 errorMessage.append(ffpe.getInput() + "\n");
			 logger.error(errorMessage.toString());
			 } else {
			 logger.error("An error has occurred", t);
			 }
	    }

	    @OnSkipInWrite
	    public void onSkipInWrite(Employee item, Throwable t) {
	    	System.out.println("******SkipListener - onSkipInWrite()*******");
	    }

	    @OnSkipInProcess
	    public void onSkipInProcess(Employee item, Throwable t) {
	    	Exception e = (Exception)t;
	    	 StringBuilder errorMessage = new StringBuilder();
			 errorMessage.append("An error occured while processing the file. Below was the faulty input. \n" );
			 errorMessage.append(item.toString() + "\n");
			 logger.error(errorMessage.toString());
	    }

}
