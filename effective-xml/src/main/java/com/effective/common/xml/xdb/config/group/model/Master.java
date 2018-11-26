package com.effective.common.xml.xdb.config.group.model;

import com.effective.common.xml.xdb.config.datasorce.model.DataSource;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


public class Master implements Serializable{
	
	private static final long serialVersionUID = -6364729741318647351L;

	private String name;
	
	private DataSource dataSource;
	
	private String className;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
