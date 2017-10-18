package com.effective.common.zk;

public class PushNode {

	private String idc;		// IDC信息 包括cmcc cutc ct

	private String ipPort;	// 发布出去的IP:PORT
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PushNode [");
		sb.append("idc=").append(idc).append(", ");
		sb.append("ipPort=").append(ipPort).append("]");
		return sb.toString();
	}
	
	public String getIdc() {
		return idc;
	}
	public void setIdc(String idc) {
		this.idc = idc;
	}
	public String getIpPort() {
		return ipPort;
	}
	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	
	
}
