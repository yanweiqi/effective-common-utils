package com.effective.common.dbutils.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnTools {

	private static String dirverClassName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8";
    private static String user = "root";
    private static String password = "root";

    public static Connection makeConnection() {
            Connection conn = null;
            try {
                    Class.forName(dirverClassName);
            } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
            try {
                    conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                    e.printStackTrace();
            }
            return conn;
    } 
}
