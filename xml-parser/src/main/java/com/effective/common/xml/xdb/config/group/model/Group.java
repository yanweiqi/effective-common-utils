package com.effective.common.xml.xdb.config.group.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class Group implements Serializable{

	private static final long serialVersionUID = 5404050859366155050L;
	
    private String name;
    private Master master;
    private Slave  slave;
	private String className;

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public Master getMaster() {return master;}
	public void setMaster(Master master) {this.master = master;}
	
	public Slave getSlave() {return slave;}
	public void setSlave(Slave slave) {this.slave = slave;}

	public void setClassName(String className) {this.className = className;}
	public String getClassName() {return className;}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
