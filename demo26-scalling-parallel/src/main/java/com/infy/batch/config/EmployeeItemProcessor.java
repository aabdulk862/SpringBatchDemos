package com.infy.batch.config;

import java.util.Random;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Processing employee with id: "+item.toString());
		System.out.println("Processing employee wtih thread: "+Thread.currentThread().getName());
		return item;
	}
	

}
