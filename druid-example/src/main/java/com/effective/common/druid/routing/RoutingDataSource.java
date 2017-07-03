package com.effective.common.druid.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/29 ${time}
 * @Description: TODO
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
    
    private static final Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);
    
    protected Object determineCurrentLookupKey() {
        logger.info(">>> determineCurrentLookupKey thread: {}", Thread.currentThread().getName() );
        logger.info(">>> RoutingDataSource: {}", DynamicDataSourceHolder.getRouteKey());
        return DynamicDataSourceHolder.getRouteKey();
    }
}
