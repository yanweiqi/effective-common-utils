package com.effective.common.xml.xdb.config.group.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class Group implements Serializable{

	private static final long serialVersionUID = 5404050859366155050L;
	
    private String name;
    private String master;
    private String salve;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getSalve() {
		return salve;
	}
	public void setSalve(String salve) {
		this.salve = salve;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	

}
