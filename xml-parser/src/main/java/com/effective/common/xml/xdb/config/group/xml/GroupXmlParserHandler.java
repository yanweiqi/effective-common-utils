package com.effective.common.xml.xdb.config.group.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.effective.common.xml.xdb.config.datasorce.model.Master;
import com.effective.common.xml.xdb.config.datasorce.model.Slave;
import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.model.Group;
import com.effective.common.xml.xdb.context.FileConfigService;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
@Named
public class GroupXmlParserHandler extends AbstarctXmlParserHandler<Group>{
	
	private static final Logger logger = LoggerFactory.getLogger(GroupXmlParserHandler.class);
	
	private static List<Group> groups = new ArrayList<Group>();
	
	private Group group;
	
	@Autowired
	private FileConfigService fileConfigService; 
	
	@Autowired
	private DataSourceXmlParserHandler dataSourceXmlParserHandler;
	
	@Inject
	public InputStream getResourceStream(){
		InputStream xmlInputStream = getClass().getResourceAsStream(fileConfigService.getGroup());
		return xmlInputStream;
	}

	@Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		logger.info("-------->startElement() uri:"+uri+",localName:"+localName+",qName:"+qName+",attributes:"+attributes.getValue("name"));
		logger.info("开始解析:"+qName);
        if("group".equalsIgnoreCase(qName)){
        	group =  new Group();
        	group.setName(attributes.getValue("name"));;
            group.setClassName(attributes.getValue("class"));
            elementStack.push(qName); 
            objectStack.push(group);
        }
        
        if("master".equalsIgnoreCase(qName)){
        	Master master = new Master();
        	master.setName(attributes.getValue("name"));
        	master.setClassName(attributes.getValue("class"));
            elementStack.push(qName); 
            objectStack.push(master);
            group.setMaster(master);
        }
    	
        if("slave".equalsIgnoreCase(qName)){
        	Slave slave = new Slave();
        	slave.setName(attributes.getValue("name"));
        	slave.setClassName(attributes.getValue("class"));
            elementStack.push(qName); 
            objectStack.push(slave);
            group.setSlave(slave);
        }
        
    	if("list".equalsIgnoreCase(qName)){
    		List<DataSource> dataSources = new ArrayList<DataSource>();
            elementStack.push(qName); 
            objectStack.push(dataSources);
            group.getSlave().setDatasources(dataSources);
    	}
    	
		if("datasource".equalsIgnoreCase(qName)){
			String ref = attributes.getValue("ref");
			DataSource dataSource = dataSourceXmlParserHandler.getDataSourceByName(ref);
            elementStack.push(qName); 
            objectStack.push(dataSource);
		}
    }
	
   
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
    
    	if("group".equalsIgnoreCase(qName)){
    		logger.info("结束解析:"+qName);
        	if(!groups.contains(group)){
        		groups.add(group);
        	}
        	elementStack.pop();
        }
    	
        if("master".equalsIgnoreCase(qName)){
           logger.info("结束解析:"+qName);
           Master master = (Master) objectStack.pop();
           if(null != master) group.setMaster(master);
           elementStack.pop();
        }
    	
        if("slave".equalsIgnoreCase(qName)){
        	logger.info("结束解析:"+qName);
    		Slave slave = (Slave) objectStack.pop();
    	    if(null != slave) group.setSlave(slave);
    	    elementStack.pop();
        }
    	
        if("list".equalsIgnoreCase(qName)){
        	logger.info("结束解析:"+qName);
        	@SuppressWarnings("unchecked")
        	List<DataSource> dataSources =  ((List<DataSource>) objectStack.pop());
        	if(null != dataSources) group.getSlave().setDatasources(dataSources);
        	elementStack.pop();
        }
        
        if("datasource".equalsIgnoreCase(qName)){
        	logger.info("结束解析:"+qName);
        	if("master".equals(currentElementParent())){
        		DataSource dataSource = (DataSource) objectStack.pop();
                if( null != dataSource){
                	Master master = group.getMaster();
                	master.setDataSource(dataSource);
                }
        	}
        	else{
            	DataSource dataSource = (DataSource)objectStack.pop();
            	Slave slave = group.getSlave();
            	if( null != dataSource){
            		List<DataSource> dataSources = slave.getDatasources();
            		dataSources.add(dataSource);
            	}
        	}
        	elementStack.pop();
        }
    	logger.info("-------->endElement()  uri:"+uri+",localName:"+localName+",qName:"+qName);
    }
    
    @Override
	public void buildBean(InputStream xmlInputStream){
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser  saxParser = factory.newSAXParser();
            saxParser.parse(xmlInputStream, this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
	}
    
    @Override
	public void buildBean(){
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser  saxParser = factory.newSAXParser();
            saxParser.parse(getResourceStream(), this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
	}

	public List<Group> getGroups() {
		return groups;
	}
    
}
