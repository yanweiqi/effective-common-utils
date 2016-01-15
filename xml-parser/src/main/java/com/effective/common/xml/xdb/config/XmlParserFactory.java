package com.effective.common.xml.xdb.config;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;


/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
@Named
public class XmlParserFactory {
	
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(XmlParserFactory.class);
        
    @Autowired
    private GroupXmlParserHandler groupXmlParserHandler;
    
    @Autowired
    private DataSourceXmlParserHandler dataSourceXmlParserHandler;
    
    @Inject
    public void build(){
    	dataSourceXmlParserHandler.buildBean();
    	groupXmlParserHandler.buildBean();
    }
    
}
