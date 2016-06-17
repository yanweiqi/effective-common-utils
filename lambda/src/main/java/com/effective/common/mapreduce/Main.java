package com.effective.common.mapreduce;

import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args){
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		
		double bill = costBeforeTax.stream().map((cost) -> cost + 0.12*cost).reduce(
				(sum,cost) -> sum + cost
		).get();
        System.out.println(bill);
	}
}
