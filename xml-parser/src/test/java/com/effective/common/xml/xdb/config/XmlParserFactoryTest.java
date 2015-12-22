package com.effective.common.xml.xdb.config;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.effective.common.xml.xdb.config.datasorce.model.DataSource;
import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.model.Group;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class XmlParserFactoryTest {

	private static final Logger logger = LoggerFactory.getLogger(XmlParserFactoryTest.class);
	
	@Test
	public void testDataSourceXmlParserHandler() {
		XmlParserFactory<DataSource, DataSourceXmlParserHandler> xpf = new XmlParserFactory<DataSource, DataSourceXmlParserHandler>();
		DataSourceXmlParserHandler groupXmlParserHandler = new DataSourceXmlParserHandler();
		
		xpf.setM(groupXmlParserHandler);
		List<DataSource> datasources = xpf.buildBean("E:\\Eclipse_WorkSpace\\yanweiqi\\effective-common-utils\\xml-parser\\src\\test\\resource\\xdb\\ds.xml");
		for (DataSource ds : datasources) {
			logger.info(ds.toString());
		}
	}
	
	@Test
	public void testGroupXmlParserHandler(){
		XmlParserFactory<Group, GroupXmlParserHandler> xpf = new XmlParserFactory<Group, GroupXmlParserHandler>();
		GroupXmlParserHandler groupXmlParserHandler = new GroupXmlParserHandler();
		xpf.setM(groupXmlParserHandler);
		List<Group> groups = xpf.buildBean("E:\\Eclipse_WorkSpace\\yanweiqi\\effective-common-utils\\xml-parser\\src\\test\\resource\\xdb\\group.xml");
		for (Group g : groups) {
			logger.info(g.toString());
		}
	}

}
