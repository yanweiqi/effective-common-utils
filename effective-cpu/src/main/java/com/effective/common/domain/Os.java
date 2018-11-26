package com.effective.common.domain;

import java.io.Serializable;

public class Os implements Serializable {

	private static final long serialVersionUID = 3136426852744098873L;

	/**
	 * 架构
	 */
	private String arch;
	
	/**
	 * 操作cpuEndian
	 */
	private String cpuEndian;
	
	/**
	 * dataModel
	 */
	private String dataModel;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 卖主
	 */
	private String vendor;
	
	/**
	 * 卖主名
	 */
	private String vendorCodeName;
	
	/**
	 * 名称
	 */
	private String vendorName;
	
	/**
	 * 卖主类型
	 */
	private String vendorVersion;
	
	/**
	 * 版本号
	 */
	private String version;

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public String getCpuEndian() {
		return cpuEndian;
	}

	public void setCpuEndian(String cpuEndian) {
		this.cpuEndian = cpuEndian;
	}

	public String getDataModel() {
		return dataModel;
	}

	public void setDataModel(String dataModel) {
		this.dataModel = dataModel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendorCodeName() {
		return vendorCodeName;
	}

	public void setVendorCodeName(String vendorCodeName) {
		this.vendorCodeName = vendorCodeName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorVersion() {
		return vendorVersion;
	}

	public void setVendorVersion(String vendorVersion) {
		this.vendorVersion = vendorVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	
}
