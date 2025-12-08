package com.infy.batch.listener;

import java.util.List;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnWriteError;

import com.infy.batch.bean.Employee;

public class MyItemWriteListener {
	@BeforeWrite
	public void beforeWrite(List < ? extends Employee > items) {
		System.out.println("******ItemWriteListener - beforeWrite()*******");
    }
	@AfterWrite
	public void afterWrite(List < ? extends Employee > items) {
		System.out.println("******ItemWriteListener - afterWrite()*******");
    }
	@OnWriteError
	public void onWriteError(Exception exception,List < ? extends Employee > items) {
		System.out.println("******ItemWriteListener - onWriteError()*******");
    }

   
}
