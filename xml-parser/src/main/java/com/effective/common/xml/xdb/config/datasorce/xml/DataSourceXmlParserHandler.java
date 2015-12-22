package com.effective.common.xml.xdb.config.datasorce.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.effective.common.xml.xdb.config.AbstarctXmlParserHandler;
import com.effective.common.xml.xdb.config.datasorce.model.DataSource;

/**
 * 
 * @author yanweiqi
 * @version v1.0.0
 * @date 2015年12月22日
 * @Copyright Copyright©2015  
 */
public class DataSourceXmlParserHandler extends AbstarctXmlParserHandler<DataSource>{
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceXmlParserHandler.class);

	@Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        logger.debug("开始解析:"+qName);
        elementStack.push(qName); // 将标签名压入栈
        if("ds".equalsIgnoreCase(qName)){
        	t = new DataSource();
        	t.setName(attributes.getValue("name"));
        	t.setDriverClassName(attributes.getValue("driverClassName"));
        	t.setUrl(attributes.getValue("url"));
        	t.setUsername(attributes.getValue("username"));
        	t.setPassword(attributes.getValue("password"));
        	t.setMaxActive(attributes.getValue("maxActive"));
        	t.setMaxIdle(attributes.getValue("maxIdle"));
        	t.setMaxWait(attributes.getValue("maxWait"));
        }
    }
	
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
    	logger.debug("解析完毕:"+qName);
    	list.add(t);
    	elementStack.pop();// 表示该元素解析完毕，需要从栈中弹出标签
    }



    
}
