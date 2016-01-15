package com.effective.common.xml.xdb.config.datasorce.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.effective.common.xml.xdb.config.AbstarctXmlParserHandler;
import com.effective.common.xml.xdb.config.datasorce.model.DataSource;
import com.effective.common.xml.xdb.context.FileConfigService;

/**
 * 
 * @author yanweiqi
 * @version v1.0.0
 * @date 2015年12月22日
 * @Copyright Copyright©2015  
 */
@Named
public class DataSourceXmlParserHandler extends AbstarctXmlParserHandler<DataSource>{
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceXmlParserHandler.class);
	
	@Autowired
	private FileConfigService fileConfigService; 
	
	private List<DataSource> datasources = new ArrayList<DataSource>();
	
	private static ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<String, DataSource>();
	
	@Inject
	public InputStream getResourceStream(){
		InputStream xmlInputStream = getClass().getResourceAsStream(fileConfigService.getDatasource());
		return xmlInputStream;
	}

	@Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        logger.debug("开始解析:"+qName);
        elementStack.push(qName); // 将标签名压入栈
        if("ds".equalsIgnoreCase(qName)){
        	group = new DataSource();
        	group.setName(attributes.getValue("name"));
        	group.setDriverClassName(attributes.getValue("driverClassName"));
        	group.setUrl(attributes.getValue("url"));
        	group.setUsername(attributes.getValue("username"));
        	group.setPassword(attributes.getValue("password"));
        	group.setMaxActive(attributes.getValue("maxActive"));
        	group.setMaxIdle(attributes.getValue("maxIdle"));
        	group.setMaxWait(attributes.getValue("maxWait"));
        }
    }
	
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
    	logger.debug("解析完毕:"+qName);
    	elementStack.pop();// 表示该元素解析完毕，需要从栈中弹出标签
    	if(!datasources.contains(group)){
    		datasources.add(group);
    		cache.put(group.getName(), group);
    	}
    }
    
    @Override
	public void buildBean(InputStream xmlInputStream){
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlInputStream, this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
	}
    
    @Override
	public void buildBean(){
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(getResourceStream(), this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
	}

	public List<DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
	}
    
	public DataSource getDataSourceByName(String name){
		return cache.get(name);
	}
    
}
