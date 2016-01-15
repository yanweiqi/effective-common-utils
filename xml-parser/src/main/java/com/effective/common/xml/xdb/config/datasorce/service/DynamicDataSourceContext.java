package com.effective.common.xml.xdb.config.datasorce.service;


public final class DynamicDataSourceContext {
	
	private ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
    private static DynamicDataSourceContext context = null;

    public static DynamicDataSourceContext getInstance() {
    	if(context == null){
    		context = new DynamicDataSourceContext();
    	}
        return context;
    }

    private DynamicDataSourceContext() {}

    public void setDataSourceKey(String dataSourceKey) {
    	System.out.println("set dataSourceKey:"+dataSourceKey);
        threadLocal.set(dataSourceKey);
    }

    public String getDataSourceKey() {
    	String dataSourceKey = threadLocal.get();
    	System.out.println("get dataSourceKey:"+dataSourceKey);
        return dataSourceKey;
    }
}
