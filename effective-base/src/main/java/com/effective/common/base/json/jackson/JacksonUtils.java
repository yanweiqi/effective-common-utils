package com.effective.common.base.json.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;



/**
 * @author yanweiqi
 * @DateTime 2016-07-06
 */
public class JacksonUtils {
	
	private static final Logger Log = LoggerFactory.getLogger(JacksonUtils.class);
	
    /**
     * 将字符串转化为对象
     * @param json
     * @param targertClass
     * @return
     * @throws Exception
     */
	public static <T> T jsonToBean(String json,Class<T> targertClass) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		T t = null;
		if(null == json){
			throw new NullPointerException("json String isnot null");
		} else {
	    	try {
				t = mapper.readValue(json,targertClass);
			} catch (JsonParseException e) {
				Log.error(e.getMessage(),e);
				throw e;
			} catch (JsonMappingException e) {
				Log.error(e.getMessage(),e);
				throw e;
			} catch (IOException e) {
				Log.error(e.getMessage(),e);
				throw e;
			}
		}
		return t;
	}
	
	/**
	 * 将对象转化为Json字符串
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static <T> String beanToJson(T bean) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		if(null == bean){
			throw new NullPointerException("bean is not null");
		} else {
			try {
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				json = mapper.writeValueAsString(bean);
			} catch (JsonProcessingException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			}
		}
		return json;
	}
	
	/**
	 * 将对象的写入json文件中
	 * @param file
	 * @param t
	 * @throws Exception
	 */
	public static <T> void beanWriteFile(File file, T t) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		if(null == file || null ==t){
			throw new NullPointerException("file and t  is not null");
		} else{
			try {
				mapper.writeValue(file, t);
			} catch (JsonGenerationException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			} catch (JsonMappingException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			} catch (IOException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			}
		}

	}
	
	/**
	 * 从json文件中读取并转化为Bean对象
	 * @param file
	 * @param targetClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T readFileToBean(File file,Class<T> targetClass) throws Exception{
		T t = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null == file || null == targetClass){
			throw new NullPointerException("file and t  is not null");
		} else {
			try {
				t = mapper.readValue(file,targetClass);
			} catch (JsonParseException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			} catch (JsonMappingException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			} catch (IOException e) {
	            Log.error(e.getMessage(),e);
	            throw e;
			}
		}
		return t;
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static String jsonToXML(String json) throws Exception{
		XmlMapper xmlMapper = new XmlMapper();
		Object obj = jsonToBean(json,  Object.class);
		return xmlMapper.writeValueAsString(obj);
	}
}
