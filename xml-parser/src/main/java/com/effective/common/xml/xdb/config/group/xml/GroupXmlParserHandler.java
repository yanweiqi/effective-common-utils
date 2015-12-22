package com.effective.common.xml.xdb.config.group.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.effective.common.xml.xdb.config.AbstarctXmlParserHandler;
import com.effective.common.xml.xdb.config.group.model.Group;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class GroupXmlParserHandler extends AbstarctXmlParserHandler<Group>{
	
	private static final Logger logger = LoggerFactory.getLogger(GroupXmlParserHandler.class);

	@Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        logger.debug("开始解析:"+qName);
        elementStack.push(qName); // 将标签名压入栈
        if("group".equalsIgnoreCase(qName)){
        	t =  new Group();
        	t.setName(attributes.getValue("name"));;
        	t.setMaster(attributes.getValue("master"));
        	t.setSalve(attributes.getValue("salve"));
        }
    }
	
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
    	logger.debug("解析完毕:"+qName);
    	list.add(t);
    	elementStack.pop();// 表示该元素解析完毕，需要从栈中弹出标签
    }
}
