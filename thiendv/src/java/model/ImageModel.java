/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Image;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 *
 * @author doanvanthien
 */
public class ImageModel extends ConnectionPool implements Serializable {

    public ImageModel() throws IOException, ClassNotFoundException, SQLException {
    }

    public Image getImage(int id) throws IOException, ClassNotFoundException, SQLException {
        Image image = new Image();
        openConnection();
        String str = "select * from images where id = " + id;
        mpreparedStatement = connection.prepareStatement(str);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            image.setId(mresultSet.getInt("id"));
            image.setImage(mresultSet.getString("image"));
        }

        closeAll();
        return image;
    }

    public int add(Image image) throws IOException, ClassNotFoundException, SQLException {
        int i = -1;
        openConnection();
        String str = "insert into images(image) values(?)";
        mpreparedStatement = connection.prepareStatement(str);
        mpreparedStatement.setString(1, image.getImage());
        i = mpreparedStatement.executeUpdate();
        closeAll();
        return i;
    }
}
