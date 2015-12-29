package com.effective.common.xml.xdb.config;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.effective.common.xml.SpringContextTestCase;
import com.effective.common.xml.xdb.config.datasorce.model.DataSource;
import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.model.Group;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;
import com.effective.common.xml.xdb.context.FileConfigService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class XmlParserFactoryTest extends SpringContextTestCase {

	private static final Logger logger = LoggerFactory.getLogger(XmlParserFactoryTest.class);
	
	@Autowired
	private FileConfigService fileConfigService;
	
	@Autowired
	private GroupXmlParserHandler groupXmlParserHandler;
	
	@Autowired
	private DataSourceXmlParserHandler dataSourceXmlParserHandler;
	
	@Test
	public void testDataSourceXmlParserHandler() {
		//InputStream xmlInputStream = getClass().getResourceAsStream(fileConfigService.getDatasource());
		//XmlParserFactory<DataSource, DataSourceXmlParserHandler> xpf = new XmlParserFactory<DataSource, DataSourceXmlParserHandler>();
		//DataSourceXmlParserHandler groupXmlParserHandler = new DataSourceXmlParserHandler();
		//xpf.setM(groupXmlParserHandler);
		dataSourceXmlParserHandler.buildBean();
		for (DataSource ds : dataSourceXmlParserHandler.getDatasources()) {
			logger.info(ds.toString());
		}
	}
	
	@Test
	public void testGroupXmlParserHandler() throws JsonGenerationException, JsonMappingException, IOException{
	    //InputStream xmlInputStream = getClass().getResourceAsStream(fileConfigService.getGroup());
		//XmlParserFactory<Group, GroupXmlParserHandler> xpf = new XmlParserFactory<Group, GroupXmlParserHandler>();
		//GroupXmlParserHandler groupXmlParserHandler = new GroupXmlParserHandler();
		//xpf.setM(groupXmlParserHandler);
		dataSourceXmlParserHandler.buildBean();
		groupXmlParserHandler.buildBean();
		for (Group g : groupXmlParserHandler.getGroups()) {
			ObjectMapper mapper = new ObjectMapper();
			Writer strWriter = new  StringWriter();
			mapper.writeValue(strWriter, g);
			logger.info(strWriter.toString());
		}
	}

	
}
