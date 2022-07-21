package com.effective.common.function;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by yanweiqi on 2017/9/13.
 */
public class FunctionTest {

    // 辅助打印方法
    private static void print(Object obj) {
        System.out.println(obj);
    }

    @Test
    public void testSupplier() {
        /**
         * 供应函数，返回一个T，只有一个get方法
         */
        Supplier<String> supplier1 = () -> "Test supplier";
        System.out.println(supplier1.get());

        Supplier<Integer> supplier2 = () -> 20;
        System.out.println(supplier2.get() instanceof Integer);
    }

    @Test
    public void testConsumer() {
        /**
         * 消费函数，有两个方法
         * 1.accept(T) 用来接收消费中传递的t,无返回值
         * 2.andThen(Consumer) 传入consumer返回consumer
         */
        // accept(T)
        AtomicReference<String> r1 = new AtomicReference<>();
        Consumer<String> c1 = (x) -> r1.set(x.toUpperCase(Locale.ROOT));
        c1.accept("ywq");
        print(String.format("1---> 入参 %s,处理后的结果 %s", "ywq", r1.get()));

        //andThen
        AtomicReference<String> r2 = new AtomicReference<>();
        Consumer<String> c2 = (x) -> r2.set("[" + x + "]");
        c1.andThen(c2).accept("jd"); //执行 c1-> c2
        print(String.format("2---> 入参 %s,处理后的结果 %s %s", "jd",r1.get(), r2.get()));
    }


    @Test
    public void testPredicate() {
        /**
         * 断言函数
         * 1.boolean test(T) 接收一个参数, 判断这个参数是否匹配某种规则, 匹配成功返回true, 匹配失败则返回false
         * 2.default方法 Predicate and(Predicate) 接收另外一个Predicate<T>类型参数进行逻辑与操作,返回一个新的Predicate
         * 3.default方法 Predicate<T> negate() 返回当前Predicate取反
         * 4.default方法 Predicate<T> or(Predicate<? super T> other), 接收另外一个Predicate<T>类型参数进行逻辑或操作
         * 5.static <T> Predicate<T> isEqual(Object targetRef)
         */
        //1.test
        Predicate<String> p1 = item -> item.equals("ywq");
        print("1---> " + p1.test("ywq"));

        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        //2.and
        Predicate<Integer> p2 = p -> p % 2 == 0;
        Predicate<Integer> p3 = p -> p > 4;
        print("2---> " + Arrays.toString(list.stream().filter(p2.and(p3)).toArray()));

        //3.or
        Predicate<Integer> p4 = t -> t < 3;
        Predicate<Integer> p5 = p4.or(t -> t > 5);
        print("3---> " + Arrays.toString(list.stream().filter(p5).toArray()));

        //4.negate
        print("4---> " + Arrays.toString(list.stream().filter(p5.negate()).toArray()));
    }

    @Test
    public void testFunction() {
        /**
         * 1.R apply(T t) 接收一个T返回一个R
         * 2.default <V> Function<V, R> compose(Function<? super V, ? extends T> before) compose接收一个Function参数先执行apply，后当前apply
         * 3.<V> Function<T, V> andThen(Function<? super R, ? extends V> after) 先执行当前apply,返回值作为参数传入after的apply()
         * 4.static <T> Function<T, T> identity() 可以返回一个输出跟输入一样的Lambda表达式对象，等价于形如 t->t形式的Lambda表达式
         */
        //apply
        Function<Integer, Integer> f1 = (x) -> x * 2;
        print(String.format("1--->apply(%d) = " + f1.apply(6), 6));

        //compose 4*4 -> 16 * 2 = 32
        Function<Integer, Integer> f2 = (x) -> x * x;
        print(String.format("2--->f1.compose(f2).apply(%d) = " + f1.compose(f2).apply(4), 4));

        //andThen 4 * 2 -> 8 * 8 = 64
        print(String.format("3--->f1.andThen(f2).apply(%d) = " + f1.andThen(f2).apply(4), 4));

        //static <T> Function<T, T> identity()
        Function<Integer, Integer> f3 = (t) -> t;
        print(String.format("4--->Function.identity().apply(%d) = " + f3.apply(4), 4));
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
