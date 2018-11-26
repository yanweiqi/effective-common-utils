package com.effective.common.base.propertity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;

@Named
public class PropertiesAccessor {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesAccessor.class);

	private final AbstractBeanFactory beanFactory;
    private final Map<String,String> cache = new ConcurrentHashMap<String, String>(); 

    @Inject 
    protected PropertiesAccessor(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    } 

    public String getProperty(String key) { 
        if(cache.containsKey(key)){ 
            return cache.get(key); 
        } 

        String foundProp = null; 
        try { 
            foundProp = beanFactory.resolveEmbeddedValue("${" + key.trim() + "}");        
            cache.put(key,foundProp);        
        } catch (IllegalArgumentException e) { 
        	logger.error(e.getMessage(),e);
        } 
        logger.debug(key+":"+foundProp);
        return foundProp; 
    } 
}