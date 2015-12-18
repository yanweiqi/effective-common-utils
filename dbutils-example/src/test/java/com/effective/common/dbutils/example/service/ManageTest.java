package com.effective.common.dbutils.example.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import com.effective.common.dbutils.example.BaseTestCase;

public class ManageTest extends BaseTestCase{
	
	@Test
	public void testInsert() throws SQLException{
        System.out.println("-------------test_insert()-------------");
        //创建连接
        Connection conn = ConnTools.makeConnection();
        //创建SQL执行工具
        QueryRunner qRunner = new QueryRunner();
        //执行SQL插入
        int n = qRunner.update(conn, "insert into user(name,pwd) values('iii','iii')");
        System.out.println("成功插入" + n + "条数据！");
        //关闭数据库连接
        DbUtils.closeQuietly(conn); 
	}

}
