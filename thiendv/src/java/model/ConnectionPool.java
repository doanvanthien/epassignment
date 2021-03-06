/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author thiendv
 */
public class ConnectionPool implements Serializable {

    protected Connection connection;
    protected PreparedStatement mpreparedStatement;
    protected ResultSet mresultSet;

    private String driverClassName;
    private String user;
    private String password;
    private String url;

    public ConnectionPool() throws IOException, ClassNotFoundException, SQLException {
//        String path = "src/java/serverconfig.properties";
//        FileInputStream inputStream = null;
//        Properties properties = new Properties();
//        inputStream = new FileInputStream(path);
//        properties.load(inputStream);
//        inputStream.close();
//        user = properties.getProperty("User");
//        password = properties.getProperty("Pass");
//        driverClassName = properties.getProperty("DriverClassName");
//        url = properties.getProperty("Url");
        user = "sa";
        password = "123456";
        driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        url = "jdbc:sqlserver://localhost\\SQLEXPRESS2012:4895;databaseName=EPAssignment";
        connection = null;
        mpreparedStatement = null;
        mresultSet = null;
    }

    public void openConnection() throws IOException, ClassNotFoundException, SQLException {

        Class.forName(driverClassName);
        connection = DriverManager.getConnection(url, user, password);
    }

    public void closeAll() throws SQLException {
        if (mpreparedStatement != null) {
            mpreparedStatement.close();
            mpreparedStatement = null;
        }

        if (mresultSet != null) {
            mresultSet.close();
            mresultSet = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getMpreparedStatement() {
        return mpreparedStatement;
    }

    public void setMpreparedStatement(PreparedStatement mpreparedStatement) {
        this.mpreparedStatement = mpreparedStatement;
    }

    public ResultSet getMresultSet() {
        return mresultSet;
    }

    public void setMresultSet(ResultSet mresultSet) {
        this.mresultSet = mresultSet;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
