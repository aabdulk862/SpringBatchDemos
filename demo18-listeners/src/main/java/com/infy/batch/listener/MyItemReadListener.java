package com.infy.batch.listener;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;

import com.infy.batch.bean.Employee;

public class MyItemReadListener {
	@BeforeRead
	public void beforeRead() {
		System.out.println("******ItemReadListener - beforeRead()*******");
    }

    @AfterRead
    public void afterRead(Employee item) {
    	System.out.println("******ItemReadListener - afterRead()*******");
    }

    @OnReadError
    public void onReadError(Exception ex) {
    	System.out.println("******ItemReadListener - onReadError()*******");
    }

}
