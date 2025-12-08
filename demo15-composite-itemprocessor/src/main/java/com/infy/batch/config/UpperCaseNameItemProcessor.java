package com.infy.batch.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class UpperCaseNameItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		item.setEmpName(item.getEmpName().toUpperCase());
		return item;
	}
	

}
