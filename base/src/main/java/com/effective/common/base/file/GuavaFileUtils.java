package com.effective.common.base.file;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * @author yanweiqi
 */
public class GuavaFileUtils {

	/**
	 * 写入文件
	 * @param fileName
	 * @param contents
	 */
	public static void write(final String fileName, final String contents) {
		checkNotNull(fileName, "Provided file name for writing must not be null.");
		checkNotNull(contents, "Unable to write null contents.");
		final File newFile = new File(fileName);
		try {
			Files.write(contents.getBytes(), newFile);
		} catch (IOException fileIoEx) {
			System.out.println("ERROR trying to write to file '" + fileName + "' - " + fileIoEx.toString());
		}
	}

	/**
	 * 读取文件,当文件大时会溢出
	 * @param filePath
	 * @throws IOException 
	 */
	public static String read(final String filePath) throws IOException{
		checkNotNull(filePath, "Provided file path for writing must not be null.");
		StringBuilder sb = new StringBuilder();
		File testFile = new File(filePath);
		List<String> lines = Files.readLines(testFile, Charsets.UTF_8);
		lines.forEach(line -> sb.append(line));
		return sb.toString();
	}
	
	/**
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void copy(final String sourceFileName,final String targetFileName) throws IOException{
		final File sourceFile = new File(sourceFileName);    
		final File targetFile = new File(targetFileName);     
		Files.copy(sourceFile, targetFile);
	}
	
	/**
	 *  比较两个文件是否相同
	 * @param sourceFileName
	 * @param targetFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean isFileSame(final String sourceFileName,final String targetFileName) throws IOException{
		final File sourceFile = new File(sourceFileName);    
		final File targetFile = new File(targetFileName);     
		return Files.equal(sourceFile, targetFile);
	}
	
   
	
}
