package com.effective.common.mapreduce;

/**
 * Created by yanweiqi on 2017/10/12.
 */
public class Person {
    public String name;

    public int age;

    Person(String name,int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d}", name, age);
    }
}