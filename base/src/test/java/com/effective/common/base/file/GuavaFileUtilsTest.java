package com.effective.common.base.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.io.Files;

public class GuavaFileUtilsTest {

	@Test
	public void testRead() throws IOException {

		List<String> lines = Files.readLines(new File("E:\\tmp\\wpx.txt"), Charset.forName("GBK"),new ToListlineProcessor(Splitter.on(","),4,""));
		for(String line:lines){
            System.out.println(line);
		}
		
	}

}
