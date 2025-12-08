package com.infy.batch.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class EmpItemProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside processor");
		return item;
	}

}
