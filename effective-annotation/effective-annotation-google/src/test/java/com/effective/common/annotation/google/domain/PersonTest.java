package com.effective.common.annotation.google.domain;

import junit.framework.TestCase;
import org.junit.Test;

public class PersonTest extends TestCase {

    @Test
    public void testTestSetName() {
        Person person = new PersonBuilder().setAge(25).setName("John").build();
        assertEquals(25, person.getAge());
        assertEquals("John", person.getName());
    }

}