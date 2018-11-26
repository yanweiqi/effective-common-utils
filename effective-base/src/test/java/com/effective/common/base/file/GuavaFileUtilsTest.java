package com.effective.common.base.file;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class GuavaFileUtilsTest {

	@Test
	public void testRead() throws IOException {

		List<String> lines = Files.readLines(new File("E:\\tmp\\wpx.txt"), Charset.forName("GBK"),new ToListlineProcessor(Splitter.on(","),4,""));
		for(String line:lines){
            System.out.println(line);
		}
		
	}

}
