package com.effective.common.xml.xdb.config.datasorce.service;

import java.util.List;

import org.aspectj.lang.JoinPoint;

public class DynamicDataSourceAspect {
	
	
    private List<String> slaveKeys;
    private int          slaveKeyIndex;

    public DynamicDataSourceAspect() {
        this.slaveKeyIndex = 0;
    }

    public List<String> getSlaveKeys() {
        return slaveKeys;
    }

    public void setSlaveKeys(List<String> slaveKeys) {
    	System.out.println("slaveKeys:"+slaveKeys.toString());
        this.slaveKeys = slaveKeys;
    }

    /**
     * 数据源切换
     */
    public synchronized void changeDataSourceSlave(JoinPoint joinPoint) {
    	Object[] parameters = joinPoint.getArgs();
    	Integer id = (Integer) parameters[0];
    	
    	//this.slaveKeyIndex = (this.slaveKeyIndex+1) % slaveKeys.size();
    	this.slaveKeyIndex = id % slaveKeys.size();
    	String slave = this.slaveKeys.get(this.slaveKeyIndex);
        System.out.println(id+","+slave);
        DynamicDataSourceContext.getInstance().setDataSourceKey(slave);
        
    }
}
