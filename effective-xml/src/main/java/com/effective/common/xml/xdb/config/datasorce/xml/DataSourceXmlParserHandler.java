package com.effective.common.xml.xdb.config.datasorce.xml;

import com.effective.common.xml.xdb.config.AbstarctXmlParserHandler;
import com.effective.common.xml.xdb.config.datasorce.model.*;
import org.slf4j.*;
import org.xml.sax.*;

import javax.inject.Named;
import java.util.*;

/**
 * 
 * @author yanweiqi
 * @version v1.0.0
 * @date 2015年12月22日
 * @Copyright Copyright©2015  
 */
@Named
public class DataSourceXmlParserHandler extends AbstarctXmlParserHandler<DataSources>{
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceXmlParserHandler.class);
	private DataSources dataSources;
	private DataSource dataSource;

	@Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        logger.debug("开始解析:"+qName);
        elementStack.push(qName);
		if("datasources".equalsIgnoreCase(qName)){
		   String clazz = attributes.getValue("class");
			try {
			    Class<DataSources> dataSourceArrayClass = (Class<DataSources>) Class.forName(clazz);
				dataSources = dataSourceArrayClass.newInstance();
				List<DataSource> dataSourceList = new ArrayList<DataSource>();
				dataSources.setName(attributes.getValue("name"));
				dataSources.setDataSourceList(dataSourceList);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

        if("dataSource".equalsIgnoreCase(qName)){
        	dataSource = new DataSource();
        	dataSource.setName(attributes.getValue("name"));
        	dataSource.setDriverClassName(attributes.getValue("driverClassName"));
        	dataSource.setUrl(attributes.getValue("url"));
        	dataSource.setUsername(attributes.getValue("username"));
        	dataSource.setPassword(attributes.getValue("password"));
        	dataSource.setMaxActive(attributes.getValue("maxActive"));
        	dataSource.setMaxIdle(attributes.getValue("maxIdle"));
        	dataSource.setMaxWait(attributes.getValue("maxWait"));
			dataSources.getDataSourceList().add(dataSource);
        }
    }
	
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
    	logger.debug("解析完毕:"+qName);
    	elementStack.pop();
    }

	public DataSources getDataSources() {
		return dataSources;
	}

	public DataSource getDataSourceByName(String name){
		List<DataSource> dataSourceList = this.dataSources.getDataSourceList();
		for(DataSource ds:dataSourceList){
			if(ds.getName().equals(name)) return ds;
		}
		return null;
	}
}
