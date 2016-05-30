package com.effective.common.xml.xdb.config.datasorce.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanweiqi on 2016/5/26.
 */
public class DataSources implements Serializable{

    private String name;
    private List<DataSource> dataSourceList;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<DataSource> getDataSourceList() {return dataSourceList;}
    public void setDataSourceList(List<DataSource> dataSourceList) {this.dataSourceList = dataSourceList; }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
