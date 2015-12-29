package com.effective.common.xml.xdb.config;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;


/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
@Service
public class XmlParserFactory {
	
    private static final Logger logger = LoggerFactory.getLogger(XmlParserFactory.class);
    
    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
    
    @Autowired
    private GroupXmlParserHandler groupXmlParserHandler;
    
    @Autowired
    private DataSourceXmlParserHandler dataSourceXmlParserHandler;
    
    
    
}
