package com.effective.common.xml.xdb.config.datasorce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M extends Thread{
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M.class);

    public static void main(String args[]){
    	Thread t1 = new T();
    	t1.start();
    	
    	Thread t2 = new T();
    	t2.start();
    	
    }
}
