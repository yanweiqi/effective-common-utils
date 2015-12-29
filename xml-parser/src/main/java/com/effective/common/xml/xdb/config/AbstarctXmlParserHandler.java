package com.effective.common.xml.xdb.config;

import java.io.InputStream;
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
	
	protected T group = null;
	
	protected String value = null;
	
    protected Stack<String> elementStack = new Stack<String>();
    
    protected Stack<Object> objectStack  = new Stack<Object>();
    
    public abstract void buildBean(InputStream xmlInputStream);
    
    public abstract void buildBean();
    
	@Override
    public abstract void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException;
	
    @Override
    public abstract void endElement(String uri, String localName, String qName) throws SAXException;

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
         value = new String(ch, start, length).trim();
         logger.info("context:"+value);
    }
    
    @Override
    public void startDocument() throws SAXException {
    	logger.info("---->startDocument() parser...");
        super.startDocument();
    }
    
    @Override
    public void endDocument() throws SAXException {
    	logger.info("---->endDocument() parser...");
        super.endDocument();
    }
	
    protected String currentElement() {
        return this.elementStack.peek();
    }

    protected String currentElementParent() {
        if(this.elementStack.size() < 2) return null;
        return this.elementStack.get(this.elementStack.size()-2);
    }
}
