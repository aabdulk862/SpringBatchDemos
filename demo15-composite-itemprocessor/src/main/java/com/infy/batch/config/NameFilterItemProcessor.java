package com.infy.batch.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Employee;

@Component
public class NameFilterItemProcessor  implements ItemProcessor<Employee, Employee>{
	private static final List<String> NAMES_TO_FILTER = Arrays.asList("Raj","Ria");

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		if(NAMES_TO_FILTER.contains(item.getEmpName()))
				return null;
		return item;
	}

}
