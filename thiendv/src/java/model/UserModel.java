/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JsfUtil;

/**
 *
 * @author thiendv
 */
public class UserModel extends ConnectionPool implements Serializable {

    public UserModel() throws IOException, ClassNotFoundException, SQLException {
    }

    public int getSerial() throws ClassNotFoundException, SQLException, IOException {
        int iResult = -1;
        openConnection();
        String strSql = "select count(*) as serial from users";
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            iResult = mresultSet.getInt("serial");
        }

        closeAll();
        return iResult;

    }

    public boolean validate(String username, String password) throws IOException, ClassNotFoundException, SQLException {
        boolean result = false;
        int id = 0;
        openConnection();
        String strSql = "select id from users where username = ? and password = CONVERT(VARCHAR(32), HashBytes('MD5', '" + password + "'), 2)";
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, username);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            id = mresultSet.getInt("id");
        }

        if (id != 0) {
            result = true;
        }

        closeAll();
        return result;
    }

    public User getUser(String username) throws IOException, ClassNotFoundException, SQLException {
        User user = new User();
        openConnection();
        String strSql = "select * from users where username = '" + username + "'";
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            user.setId(mresultSet.getInt("id"));
            user.setUsername(mresultSet.getString("username"));
            user.setPassword(mresultSet.getString("password"));
            user.setEmail(mresultSet.getString("email"));
        }
        closeAll();
        return user;
    }

    public String convertToMD5(String password) throws IOException, ClassNotFoundException, SQLException {
        String result = null;
        openConnection();
        String strSql = "select CONVERT(VARCHAR(32), HashBytes('MD5', '" + password + "'), 2) as password";
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            result = mresultSet.getString("password");
        }

        closeAll();
        return result;
    }

    public int changePassword(String newPassword, int user_id) throws IOException, ClassNotFoundException, SQLException {
        int iResult = -1;
        
        openConnection();
        String strSql = "update users set password = CONVERT(VARCHAR(32), HashBytes('MD5', '" + newPassword + "'), 2) where id = " + user_id;
        mpreparedStatement = connection.prepareStatement(strSql);
        iResult = mpreparedStatement.executeUpdate();

        closeAll();
        return iResult;
    }

    public List<User> getAllUser() throws IOException, ClassNotFoundException, SQLException {
        List<User> result = new ArrayList<>();
        String strSql = "select * from users";
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            User user = new User();
            user.setUsername(mresultSet.getString("username"));
            user.setEmail(mresultSet.getString("email"));
            result.add(user);
        }
        closeAll();
        return result;
    }
    
    public int addUser(User user) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        int result = -1;
        String password = JsfUtil.convertToMD5(user.getPassword());
        String strSql = "insert into users(username,password,email) values(?,?,?)";
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, user.getUsername());
        mpreparedStatement.setString(2, password);
        mpreparedStatement.setString(3, user.getEmail());
        result = mpreparedStatement.executeUpdate();
        closeAll();
        return result;
    }
}
