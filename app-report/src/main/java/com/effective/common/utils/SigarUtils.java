package com.effective.common.utils;

import java.io.File;

import com.google.common.io.Resources;

public class SigarUtils {
	
	private static String PROJECT_NAME = "hyperic-sigar-1.6.4";
	private static String PROJECT_BIN = "sigar-bin";

	private static StringBuilder sigarPath = new StringBuilder();
	
	public static String getSigarPath(){
		String filePath = Resources.getResource(PROJECT_NAME).getFile();
		sigarPath.append(filePath)
			.append(File.separator)
			.append(PROJECT_BIN)
			.append(File.separator)
			.append("lib");

		return sigarPath.toString();
	}
	
	public static void main(String[] args){
		System.out.println(getSigarPath());
	}
}
