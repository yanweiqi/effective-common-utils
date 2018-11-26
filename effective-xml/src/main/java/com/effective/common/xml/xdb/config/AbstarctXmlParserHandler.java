package com.effective.common.xml.xdb.config;

import com.effective.common.xml.xdb.context.FileConfigService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.inject.Inject;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.Stack;

/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public abstract class  AbstarctXmlParserHandler<T>  extends DefaultHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstarctXmlParserHandler.class);

    protected Stack<String> elementStack = new Stack<String>();
    protected Stack<Object> objectStack  = new Stack<Object>();

    @Autowired
    protected FileConfigService fileConfigService;

    @Inject
    protected InputStream getResourceStream(){
        InputStream xmlInputStream = getClass().getResourceAsStream(fileConfigService.getDatasource());
        return xmlInputStream;
    }

    protected void buildBean(InputStream xmlInputStream){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlInputStream, this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
    }

    protected void buildBean(){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser  saxParser = factory.newSAXParser();
            saxParser.parse(getResourceStream(), this);
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        logger.info("context:"+value);
    }
    
    @Override
    public void startDocument() throws SAXException {
    	logger.info("---->startDocument()");
        super.startDocument();
    }
    
    @Override
    public void endDocument() throws SAXException {
    	logger.info("---->endDocument()");
        super.endDocument();
    }

    /**
     * 当前节点
     * @return
     */
    protected String currentElement() {
        return elementStack.peek();
    }

    /**
     * 当前节点的父节点
     * @return
     */
    protected String currentElementParent() {
        if(elementStack.size() < 2) return null;
        return elementStack.get(elementStack.size()-2);
    }
}
