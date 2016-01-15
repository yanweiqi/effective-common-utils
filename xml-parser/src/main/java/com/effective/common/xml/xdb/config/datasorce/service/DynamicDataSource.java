package com.effective.common.xml.xdb.config.datasorce.service;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 
 * @author yanweiqi
 * @date 2015年11月3日
 * @time 上午9:18:52
 * @Copyright Copyright©2015 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
    @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContext.getInstance().getDataSourceKey();
    }


}
