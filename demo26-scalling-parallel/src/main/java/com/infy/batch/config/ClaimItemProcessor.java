package com.infy.batch.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.infy.batch.bean.Claim;

@Component
public class ClaimItemProcessor implements ItemProcessor<Claim, Claim> {

	@Override
	public Claim process(Claim item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Processing claim with id : "+item.toString());
		System.out.println("Processing the claim wtih thread: "+Thread.currentThread().getName());
		return null;
	}

}
