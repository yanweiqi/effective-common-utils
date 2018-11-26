package com.effective.common.base.json.jackson;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtilsTest {
	
	private static final Logger Log = LoggerFactory.getLogger(JacksonUtilsTest.class);

	@Test
	public void testJsonToBean() throws Exception {
        Student student = new Student();
        //student.setAge(10);
       // student.setName("Mahesh");
		String json = JacksonUtils.beanToJson(student);
		Log.info(json);
	}

	@Test
	public void testBeanToJson() throws Exception {
		String json = " {\"name\":\"Mahesh\",\"age\":10}";
		Student s = JacksonUtils.jsonToBean(json, Student.class);
		Log.info(s.getName());
	}
	
	@Test
	public void testJsonToXML() throws Exception{
		String json = " {\"name\":\"Mahesh\",\"age\":10}";
		String xml = JacksonUtils.jsonToXML(json);
		Log.info(xml);
	}
}

class Student{
	private String name;
	private int age;

	public Student() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String toString() {
		return "Student [ name: " + name + ", age: " + age + " ]";
	}
}