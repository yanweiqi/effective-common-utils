package com.effective.common.druid.routing;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/29 ${time}
 * @Description: TODO
 */
public class DynamicDataSourceHolder {
    
    private static final ThreadLocal<DataSourceEnum> routeKey = new ThreadLocal();

    /**
     * 获取当前线程的数据源路由的key
     */
    public static DataSourceEnum getRouteKey() {
        return routeKey.get();
    }

    /**
     * 绑定当前线程数据源路由的key
     * 使用完成后必须调用removeRouteKey()方法删除
     */
    public static void  setRouteKey(DataSourceEnum dataSourceEnum) {
        routeKey.set(dataSourceEnum);
    }
    
    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void removeRouteKey() {
        routeKey.remove();
    }
}
