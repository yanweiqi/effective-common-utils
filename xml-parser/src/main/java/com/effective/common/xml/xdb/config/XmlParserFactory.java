package com.effective.common.xml.xdb.config;

import com.effective.common.xml.xdb.config.datasorce.xml.DataSourceXmlParserHandler;
import com.effective.common.xml.xdb.config.group.xml.GroupXmlParserHandler;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.*;


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
