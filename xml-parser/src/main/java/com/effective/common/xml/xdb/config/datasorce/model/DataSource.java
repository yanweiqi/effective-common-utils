/**
 * 
 */
package com.effective.common.xml.xdb.config.datasorce.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author yanweiqi
 * @version v1.0.0
 * @date 2015年12月22日
 * @Copyright Copyright©2015  
 */
public class DataSource implements Serializable {

    private static final long serialVersionUID = 3370406254232535134L;
    private String name;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String maxActive;
    private String maxIdle;
    private String maxWait;

    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	public String getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
    
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
   
}
