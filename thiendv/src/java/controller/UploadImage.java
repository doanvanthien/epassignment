/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.tempClass.encodeImage;
import entity.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.ImageModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author doanvanthien
 */
@ManagedBean
@ViewScoped
public class UploadImage {

    private UploadedFile file;

    public UploadImage() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            InputStream imageInFile = file.getInputstream();
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(new File("C:\\Users\\doanvanthien\\Desktop\\motivation.png"));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = imageInFile.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            File mfile = new File("C:\\Users\\doanvanthien\\Desktop\\motivation.png");

            // Reading a Image file from file system
            FileInputStream imageIn = new FileInputStream(mfile);
            byte imageData[] = new byte[(int) mfile.length()];
            imageIn.read(imageData);

            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);
            System.out.println("Done!");

            Image image = new Image();
            image.setImage(imageDataString);
            ImageModel im = new ImageModel();
            if (im.add(image) > 0) {
                JsfUtil.addSuccessMessage("Add successfully");
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
