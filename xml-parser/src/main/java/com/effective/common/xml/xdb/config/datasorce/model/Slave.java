package com.effective.common.xml.xdb.config.datasorce.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Slave implements Serializable{
	
	private static final long serialVersionUID = -776598079604035429L;

	private String name;
	
	private List<DataSource> datasources ;
	
	private String className;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
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
