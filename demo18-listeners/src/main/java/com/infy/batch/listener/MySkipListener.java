package com.infy.batch.listener;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;

public class MySkipListener {
	 @OnSkipInRead
	    public void onSkipInRead(Throwable t) {
	        System.out.println("******SkipListener - onSkipInRead()*******");
	    }

	    @OnSkipInWrite
	    public void onSkipInWrite(Number item, Throwable t) {
	    	System.out.println("******SkipListener - onSkipInWrite()*******");
	    }

	    @OnSkipInProcess
	    public void onSkipInProcess(String item, Throwable t) {
	    	System.out.println("******SkipListener - onSkipInProcess()*******");
	    }

}
