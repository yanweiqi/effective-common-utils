package com.effective.common.base.object;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class Operators {

    @Test
    public void testCompare() {

        Integer a0 = Integer.valueOf(1);
        assertThat(1 == a0).isTrue();
        System.out.println(String.format(" 1---> new Integer(1) == 1 ? %b",1 == a0));

        Integer a1 = Integer.valueOf(1);
        Integer b1 = Integer.valueOf(1);
        assertThat(a1 == b1).isFalse();
        System.out.println(String.format(" 2---> new Integer(1) == new Integer(1) ? %b",a1 == a0));

        Integer a2 = Integer.valueOf(1);
        Integer b2 =  Integer.valueOf(1);
        assertThat(a2 == b2).isTrue();
        System.out.println(String.format(" 3---> Integer.valueOf(1) == Integer.valueOf(1) ? %b",a2 == b2));


        System.out.println(String.format(" 4---> Hello! == Hello! ? %b","Hello!" == "Hello!"));
    }
}
