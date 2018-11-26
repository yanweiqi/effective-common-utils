package com.effective.common.xml.xdb.context;

import com.effective.common.xml.PropertiesAccessor;
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
public class FileConfigService{
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FileConfigService.class);
	
    @Autowired
	private PropertiesAccessor propertiesAccessor;
	
	private String group      = "group.xml";
	private String datasource = "ds.xml";
	private String ehcache    = "ehcache.xml";
	private String redis      = "redis.xml";
	private String memcache   = "memcache.xml";
	private String sqlmap     = "sqlmap.xml";
	
	@Inject
	public void loadingXmlFilePath(){
		if(propertiesAccessor.getProperty("GROUP_FILE") != null && !propertiesAccessor.getProperty("GROUP_FILE").equalsIgnoreCase("")){
		   this.group = propertiesAccessor.getProperty("GROUP_FILE");
		}
		if(propertiesAccessor.getProperty("DATA_SOURCE_FILE")!= null && !propertiesAccessor.getProperty("DATA_SOURCE_FILE").equalsIgnoreCase("")){
			this.datasource = propertiesAccessor.getProperty("DATA_SOURCE_FILE");
		}
		if(propertiesAccessor.getProperty("EHCACHE_FILE") != null && !propertiesAccessor.getProperty("EHCACHE_FILE").equalsIgnoreCase("")){
		   this.ehcache = propertiesAccessor.getProperty("EHCACHE_FILE");
		}
		if (propertiesAccessor.getProperty("REDIS_FILE") != null && !propertiesAccessor.getProperty("REDIS_FILE").equalsIgnoreCase("")) {
			this.redis = propertiesAccessor.getProperty("REDIS_FILE");
		}
		if (propertiesAccessor.getProperty("MEMCACHE_FILE") != null && !propertiesAccessor.getProperty("MEMCACHE_FILE").equalsIgnoreCase("")) {
			this.memcache = propertiesAccessor.getProperty("MEMCACHE_FILE");
		}
		if (propertiesAccessor.getProperty("SQLMAP_FILE") != null && !propertiesAccessor.getProperty("SQLMAP_FILE").equalsIgnoreCase("")) {
			this.sqlmap = propertiesAccessor.getProperty("SQLMAP_FILE");
		}
	}

	public PropertiesAccessor getPropertiesAccessor() {	return propertiesAccessor;}
	public void setPropertiesAccessor(PropertiesAccessor propertiesAccessor) {this.propertiesAccessor = propertiesAccessor;}

	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}

	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getEhcache() {	
		return ehcache;
	}
	public void setEhcache(String ehcache) {
		this.ehcache = ehcache;
	}

	public String getRedis() {
		return redis;
	}
	public void setRedis(String redis) {
		this.redis = redis;
	}

	public String getMemcache() {
		return memcache;
	}
	public void setMemcache(String memcache) {
		this.memcache = memcache;
	}

	public String getSqlmap() {
		return sqlmap;
	}
	public void setSqlmap(String sqlmap) {
		this.sqlmap = sqlmap;
	}
}
