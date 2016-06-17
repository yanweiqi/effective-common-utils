package com.effective.common.concurrent.execute;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EchoServer implements Runnable {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Override
	public void run() {
		
        try {
        	System.out.println("当前线程:"+Thread.currentThread().getName()+",执行时间:"+sdf.format(new Date()));
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
