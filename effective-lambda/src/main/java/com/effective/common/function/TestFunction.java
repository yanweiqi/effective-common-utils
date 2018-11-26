package com.effective.common.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by yanweiqi on 2017/9/13.
 */
public class TestFunction {

    public static void main(String[] args){
        testFunction();

        testSupplier();

        testConsumer();

        testPredicate();

        testCustomLamda();
    }

    public static void testFunction(){

        Function<Integer, String> function1 = (x) -> "test result: " + x;

        Function<String, String> function2 = (x) -> "after function1";

        System.out.println(function1.apply(6));

        System.out.println(function1.andThen(function2).apply(6));
    }


    public static void testSupplier(){

        Supplier<String> supplier1 = () -> "Test supplier";
        System.out.println(supplier1.get());

        Supplier<Integer> supplier2 = () -> 20;
        System.out.println(supplier2.get() instanceof Integer);
    }


    public static void testConsumer(){
        Consumer<String> consumer1 = (x) -> System.out.print(x);
        Consumer<String> consumer2 = (x) -> System.out.println(" after consumer 1");
        consumer1.andThen(consumer2).accept("test consumer1");
    }


    public static void testPredicate(){
        Predicate<String> predicate = (x) -> x.length() > 0;
        System.out.println(predicate.test("String"));
    }


    public static void testCustomLamda(){
        Consumer<String> consumer = (x) -> System.out.println("test" + x);

        CustomLamda<String> customLamda = (x) -> {
            x.accept("6");
            return "6";
        };
        customLamda.testCustomFunction(consumer);
    }
}
