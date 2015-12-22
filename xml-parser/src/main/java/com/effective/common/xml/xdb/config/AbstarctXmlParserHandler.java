package com.effective.common.xml.xdb.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public abstract class  AbstarctXmlParserHandler<T>  extends DefaultHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstarctXmlParserHandler.class);

	protected List<T> list = new ArrayList<T>();
	
	protected T t = null;
	
	protected String value = null;
	
    protected Stack<String> elementStack = new Stack<String>();
    
	@Override
    public abstract void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException;
	
    @Override
    public abstract void endElement(String uri, String localName, String qName) throws SAXException;

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
         value = new String(ch, start, length).trim();
         logger.debug("value:"+value);
    }

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
    
}
