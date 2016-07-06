package com.effective.common.domain;

import java.io.Serializable;
import java.text.NumberFormat;

public class Cpu implements Serializable{
   

	private static final long serialVersionUID = 6342528380772926961L;
	
	private static NumberFormat format = NumberFormat.getPercentInstance();
	
	/**
	 * 索引
	 */
	private int index;
	
	/**
	 * 处理频率
	 */
	private double totalMhz;
	
	/**
	 * 生产商
	 */
	private String productFactory;
	
	/**
	 * 类别
	 */
	private String model;
	
	/**
	 * 缓存
	 */
	private double cache;
	
	/**
	 * 用户使用率
	 */
	private double userUse;
	
	/**
	 * 系统使用率
	 */
	private double systemUse;
	
	/**
	 * 等待率
	 */
	private double wait;
	
	/**
	 * 错误率
	 */
	private double nick;
	
	/**
	 * 空闲率
	 */
	private double idle;
	
	/**
	 * 总的使用率
	 */
	private double totalUse;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getTotalMhz() {
		return totalMhz;
	}

	public void setTotalMhz(double totalMhz) {
		this.totalMhz = totalMhz;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getCache() {
		return cache;
	}

	public void setCache(double cache) {
		this.cache = cache;
	}

	public double getUserUse() {
		return userUse;
	}

	public void setUserUse(double userUse) {
		this.userUse = userUse;
	}

	public double getSystemUse() {
		return systemUse;
	}

	public void setSystemUse(double systemUse) {
		this.systemUse = systemUse;
	}

	public double getWait() {
		return wait;
	}

	public void setWait(double wait) {
		this.wait = wait;
	}

	public double getNick() {
		return nick;
	}

	public void setNick(double nick) {
		this.nick = nick;
	}

	public double getIdle() {
		return idle;
	}

	public void setIdle(double idle) {
		this.idle = idle;
	}

	public double getTotalUse() {
		return totalUse;
	}

	public void setTotalUse(double totalUse) {
		this.totalUse = totalUse;
	}
		
	public String getProductFactory() {
		return productFactory;
	}

	public void setProductFactory(String productFactory) {
		this.productFactory = productFactory;
	}

	/**
	 * 百分比格式化
	 * @param d
	 * @return
	 */
	public String format(double d){
		return format.format(d);
	}
	
	
	
}
