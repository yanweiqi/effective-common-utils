package com.effective.common.xml.xdb.config.group.model;

import com.effective.common.xml.xdb.config.datasorce.model.DataSource;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Slave implements Serializable{
	
	private static final long serialVersionUID = -776598079604035429L;

	private String name;
	private List<DataSource> datasources ;
	private String className;


	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public List<DataSource> getDatasources() {
		return datasources;
	}
	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}
}
