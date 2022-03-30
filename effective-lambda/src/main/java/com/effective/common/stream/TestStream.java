package com.effective.common.stream;

import org.junit.Test;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestStream {

    @Test
    public void t2(){
        AtomicInteger i = new AtomicInteger(0);
        Stream<String> s = Stream.generate(() -> "element").limit(10);
        s.forEach(x->{
            System.out.println(i.get()+":"+x);
            i.incrementAndGet();
        });
    }


    @Test
    public void t1(){
        //每次递增2，循环20次
        Stream<Integer> streamIterated = Stream.iterate(40, n -> n + 2).limit(20);
        AtomicInteger i = new AtomicInteger(0);
        streamIterated.forEach(x->{
            System.out.println(i.get()+":"+x);
            i.incrementAndGet();
        });
    }

    @Test
    public void t3(){
        AtomicInteger i = new AtomicInteger(0);
        Random random = new Random();
        //产生三个随机数
        DoubleStream doubleStream = random.doubles(3);
        doubleStream.forEach(x->{
            System.out.println(i.get()+":"+x);
            i.incrementAndGet();
        });
    }

    @Test
    public void t4(){
        AtomicInteger a = new AtomicInteger(0);
        IntStream streamOfChars = "abc".chars();

        //Stream<Integer> streamOfChars = Pattern.compile(", ").splitAsStream("a, b, c");
        streamOfChars.forEach(x->{
            System.out.println(a.get()+":"+x);
            a.incrementAndGet();
        });

        AtomicInteger i = new AtomicInteger(0);
        Stream<String> streamOfString = Pattern.compile(", ").splitAsStream("a, b, c");
        streamOfString.forEach(x->{
            System.out.println(i.get()+":"+x);
            i.incrementAndGet();
        });
    }

    @Test
    public void testFind(){
        Stream<String> stream = Stream.of("a", "b", "c").filter(element -> element.contains("b"));
        Optional<String> anyElement = stream.findAny();
        System.out.println(anyElement.get());

        Optional<String> firstElement = stream.findFirst();
        //System.out.println(firstElement.get());
    }
}
