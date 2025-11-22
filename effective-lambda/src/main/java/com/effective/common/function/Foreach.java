package com.effective.common.function;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Foreach {

	public static void main(String[] args) {
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		
		features.forEach(n -> System.out.println(n));
		
		List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
		filter(languages, (str)->str.startsWith("J"));

	}
	
	
	 public static void filter(List<String> names, Predicate<String> condition) {
	    for(String name: names)  {
	       if(condition.test(name)) {
	          System.out.println(name + " ");
	       }
	    }
	 }
		

}
