package com.effective.common.zk;

/**
 * Push Node Change Monitor
 * @author liuzhankun
 * @date 2013-7-18
 *
 */
public class PushNodeMonitor {
	
	public PushNodeMonitor(String zks, String path) {
		Monitor monitor = new Monitor(zks, path);
		monitor.start();
	}

	public static void main(String args[]){

		Monitor monitor = new Monitor("localhost:2181", "/test");
		monitor.start();
	}
}
