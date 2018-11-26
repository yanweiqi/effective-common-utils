package com.effective.common.utils;

import com.effective.common.domain.OSType;
import com.google.common.io.Resources;

public class ResourceUtils {
	
	private static OSType oSType;
	private static String PROJECT_NAME = "hyperic-sigar-1.6.4";
	private static String PROJECT_BIN = "sigar-bin";
	public  static String CLASS_PATH = "java.library.path";	

	/**
	 * 在项目中获取“hyperic-sigar-1.6.4”的绝对路径
	 * @return
	 */
	public static String getSigarPath(){
		StringBuilder sigarPath = new StringBuilder();
		String filePath = Resources.getResource(PROJECT_NAME).getFile().replaceFirst("/", "");
		String separator = getOSType() == OSType.Windows ? "/" :"\\";
		sigarPath.append(filePath)
			.append(separator)
			.append(PROJECT_BIN)
			.append(separator)
			.append("lib");
		return sigarPath.toString();
	}
	
	/**
	 * 获取操作系统类型
	 * @return
	 */
	public static OSType getOSType(){
		if (oSType == null) {
			String OS = System.getProperty("os.name", "generic").toLowerCase();
			if (OS.indexOf("win") >= 0) {
				oSType = OSType.Windows;
			} else if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
				oSType = OSType.Mac;
			} else if (OS.indexOf("nux") >= 0) {
				oSType = OSType.Linux;
			} else {
				oSType = OSType.Other;
			}
		}
		return oSType;
	}
	
	/**
	 * 添加系统环境变量
	 * @param name 环境变量的key
	 * @param path 新添加path的value
	 */
	public static void addSystemEnv(String name,String path,OSType oSType){
		String classPath = getClassPath();
		String newPath = new String(path);
		if(getOSType() == OSType.Windows){
			newPath += ";";
		}
		else{
			newPath += ":";
		}
		newPath += classPath;
		System.setProperty(name, newPath);
	}
	
	public static String getClassPath(){
		return System.getProperty(CLASS_PATH);
	}
	
	public static void main(String[] args){
		System.out.println(getSigarPath());
		System.out.println(getOSType());
		System.out.println(getClassPath());
		addSystemEnv(CLASS_PATH,getSigarPath(),getOSType());
		System.out.println(getClassPath());
	}
}
