package com.effective.common.mapreduce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args){

		/**
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		
		double bill = costBeforeTax.stream().map((cost) -> cost + 0.12*cost).reduce(
				(sum,cost) -> sum + cost
		).get();
        System.out.println(bill);
		 **/

        System.out.println("-------------------");

        Person person = new Person("ywq",23);
        Person person1 = new Person("222",12);

		Person person5 = new Person("111",15);
		Person person2 = new Person("2122",3);
		Person person3 = new Person("333",4);
		Person person4 = new Person("44",6);


        List<Person> people = new ArrayList<>();
        people.add(person);
        people.add(person1);
        people.add(person2);
        people.add(person3);
        people.add(person4);
        people.add(person5);

		//Map<String, Integer> result = people.stream().collect(Collectors.toMap(p -> p.name, p -> p.age, (exsit, newv) -> newv));

		//System.out.println(result.toString());

		Map<Integer, List<Person>> peropleByAge = people.stream().filter(p -> p.age > 12).collect(Collectors.groupingBy(p -> p.age, Collectors.toList()));

		System.out.println(peropleByAge.toString());

		Map<Integer, List<String>> peropleByAge2 = people.stream().collect(Collectors.groupingBy(p -> p.age, Collectors.mapping((Person p) -> p.name, Collectors.toList())));

		System.out.println(peropleByAge2);
	}
}


