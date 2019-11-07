package com.effective.common.akka.part1.msg;

import lombok.Data;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-1 6:15 PM
 */
@Data
public final class Employee {

    private final String name;

    private final int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

}
