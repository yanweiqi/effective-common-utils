package com.effective.common.base.date;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import com.effective.common.base.output.Print;

public class LocalDateUtilsTest extends Print {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void testConvertToDate() throws Exception {
	   Date date =	LocalDateUtils.convertToDate("2015-05-06");
	   println(sdf.format(date));
	}

	@Test
	public void testSetYear(){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime newDate = LocalDateUtils.setYear(now, 2000);
		println(newDate);
	}
}
