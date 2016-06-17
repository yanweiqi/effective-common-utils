package com.effective.common.concurrent.execute;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Schedule {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	private static DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
	
	private static ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * 按指定频率周期执行某个任务 <br>
	 * 初始化延迟0ms开始执行，每隔5ms重新执行一次任务。
	 */
	public void fixedRate(){
		excutor.scheduleAtFixedRate(new EchoServer(), //执行线程
									0,  //初始化延迟
									5000, //两次开始的执行的最小时间间隔
									TimeUnit.MILLISECONDS //计时单位
									);
	}
	
	/**
	 * 
	 */
	public void fixDelay(){
		excutor.scheduleWithFixedDelay(new EchoServer(),//执行线程 
				0, //初始化延迟
				5000, //前一次执行结束到下一次执行开始的间隔时间
				TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 每天晚上8点执行一次
	 */
    public void dayOfDelay(String time){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
        long oneDay = 24 * 60 * 60 * 1000;  
        long initDelay  = getTimeMillis("20:00:00") - System.currentTimeMillis();  
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;  
        executor.scheduleAtFixedRate(  
                new EchoServer(),  
                initDelay,  
                oneDay,  
                TimeUnit.MILLISECONDS); 
    }
    
        
    /**
     * 获取给定时间对应的毫秒数
     * @param string "HH:mm:ss"
     * @return
     */
	private static long getTimeMillis(String time) {        
        try {
			Date currentDate = dateFormat.parse(dayFormat.format(new Date()) + " " +time);
			return currentDate.getTime() ;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args){
		
		Schedule schedule = new Schedule();
		schedule.fixedRate();
		schedule.fixDelay();
		
	}
    
}
