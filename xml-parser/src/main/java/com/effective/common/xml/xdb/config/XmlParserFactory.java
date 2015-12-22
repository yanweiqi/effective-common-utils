package com.effective.common.xml.xdb.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @author yanweiqi
* @version v1.0.0
* @date 2015年12月22日
* @Copyright Copyright©2015   
*/
public class XmlParserFactory<T, M extends AbstarctXmlParserHandler<T>> {
	
    private static final Logger logger = LoggerFactory.getLogger(XmlParserFactory.class);
    
    private M m;
   
	public List<T> buildBean(String ds_path){
		List<T> list = null;
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
            InputStream xmlInput = new FileInputStream(ds_path);
            SAXParser  saxParser = factory.newSAXParser();
            saxParser.parse(xmlInput, m);
            list = m.getList();
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
        }
		return list;
	}
	
	public M getM() {
		return m;
	}

	public void setM(M m) {
		this.m = m;
	}

    
}
