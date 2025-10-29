/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.connectiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 *
 * @author pablo
 */
//Clase utilizada para generar la pool de conexiones
public class DBConnectionSingleton {

    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String SCHEMA = "cinemappdb";
    private static final String USER_NAME = "admindba";
    private static final String PASSWORD = "12345";
    private static final String URL = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + SCHEMA;

    private static DBConnectionSingleton instance;

    private DataSource datasource;

    private DBConnectionSingleton() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            PoolProperties p = new PoolProperties();
            p.setUrl(URL);
            p.setDriverClassName("com.mysql.cj.jdbc.Driver");
            p.setUsername(USER_NAME);
            p.setPassword(PASSWORD);
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                    + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            datasource = new DataSource(p);
            datasource.setPoolProperties(p);
        } catch (ClassNotFoundException ex) {
            System.getLogger(DBConnectionSingleton.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    //Metodo utilizado para obtener la conexion a la base de datos
   public Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException ex) {
            System.getLogger(DBConnectionSingleton.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
   //Metodo utilizado para obtener la instancia de conexion
    public static DBConnectionSingleton getInstance() {
        if (instance == null) {
            instance = new DBConnectionSingleton();
        }
        return instance;
    }
}
