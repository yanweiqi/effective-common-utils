package com.effective.common.function;


import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by yanweiqi on 2017/9/13.
 */
public class FunctionTest {

    @Test
    public void testSupplier() {

        Supplier<String> supplier1 = () -> "Test supplier";
        System.out.println(supplier1.get());

        Supplier<Integer> supplier2 = () -> 20;
        System.out.println(supplier2.get() instanceof Integer);
    }

    @Test
    public void testConsumer() {
        Consumer<String> c1 = (x) -> System.out.println("c1:"+x);
        Consumer<String> c2 = (x) -> System.out.println("c2:"+x);
        c1.andThen(c2).accept("-888-");

        c1.accept("222");
    }

    @Test
    public void testPredicate() {
        Predicate<String> predicate = (x) -> x.length() > 0;
        System.out.println(predicate.test("String"));
    }

    @Test
    public void testFunction() {
        Function<String, String> f1 = (x) -> "f1: " + x;
        Function<String, String> f2 = (x) -> "f2: " + x;
        //先执行参数,再执行调用者
        System.out.println(f1.compose(f2).apply("8888"));

        //先执行调用者，再执行参数，和compose相反。
        System.out.println(f1.andThen(f2).apply("8888"));
    }

    @Test
    public void testCustomLamda() {
        Consumer<String> consumer = (x) -> System.out.println("test" + x);

        CustomLamda<String> customLamda = (x) -> {
            x.accept("6");
            return "6";
        };
        customLamda.testCustomFunction(consumer);
    }
}
