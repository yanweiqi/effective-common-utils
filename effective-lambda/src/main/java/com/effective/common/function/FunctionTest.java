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
    public void testCompose() {
        Function<Integer, Integer> f1 = (x) -> x * 2;
        Function<Integer, Integer> f2 = (x) -> x * x;
        //先执行参数,再执行调用者 16 * 2 = 32
        System.out.println(f1.compose(f2).apply(4));
    }

    @Test
    public void testAndThen(){
        Function<Integer, Integer> f1 = (x) -> x * 2;
        Function<Integer, Integer> f2 = (x) -> x * x;
        //先执行调用者，再执行参数，和compose相反。8 * 8 =64
        System.out.println(f1.andThen(f2).apply(4));
    }

    @Test
    public void testCustomLambda() {
        Consumer<String> consumer = (x) -> System.out.println("test" + x);

        CustomLambda<String> customLambda = (x) -> {
            //x.accept("6");
            return "6";
        };
        customLambda.testCustomFunction(consumer);
    }
}
