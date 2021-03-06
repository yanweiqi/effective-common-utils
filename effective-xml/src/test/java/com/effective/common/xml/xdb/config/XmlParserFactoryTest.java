package com.effective.common.xml.xdb.config;

import com.effective.common.xml.SpringContextTestCase;
import com.effective.common.xml.xdb.config.datasorce.model.*;
import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.model.Group;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class XmlParserFactoryTest extends SpringContextTestCase {

	private static final Logger logger = LoggerFactory.getLogger(XmlParserFactoryTest.class);
	
	@Autowired
	private GroupXmlParserHandler groupXmlParserHandler;
	
	@Autowired
	private DataSourceXmlParserHandler dataSourceXmlParserHandler;
	
	@Test
	public void testDataSourceXmlParserHandler() {
	    DataSources dataSources = dataSourceXmlParserHandler.getDataSources();
		for (DataSource ds :dataSources.getDataSourceList()) {
			logger.info(ds.toString());
		}
	}
	
	@Test
	public void testGroupXmlParserHandler() throws JsonGenerationException, JsonMappingException, IOException{
		List<Group> groups = groupXmlParserHandler.getGroups();
		for (Group g : groups) {
			ObjectMapper mapper = new ObjectMapper();
			Writer strWriter = new  StringWriter();
			mapper.writeValue(strWriter, g);
			logger.info(strWriter.toString());
		}
	}

	
}
