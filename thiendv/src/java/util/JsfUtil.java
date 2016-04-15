/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import static controller.tempClass.encodeImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParsePosition;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author thiendv
 */
public class JsfUtil {

    public static String convertToMD5(String string) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        String result = sb.toString().toUpperCase();

        return result;
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    public static String getUserId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("userid");
        } else {
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////
    public static void setSessionValue(String strKey, Object objValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(strKey, objValue);
    }

    ////////////////////////////////////////////////////////////////////////
    public static void destroySession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    public static HttpSession getSession() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(true);
        return session;
    }

    public static void navigate(String page) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, page);
    }

    public static String getSessionId() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(true);
        return session.getId();
    }

    ////////////////////////////////////////////////////////////////////////
    public static Object removeSession(String strKey) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().remove(strKey);
    }

    public static void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    }

    public static void addWarnMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
    }

    public static void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
    }

    public static String randomPassword() {
        String result = "1";
        while (result.length() != 5) {
            SecureRandom random = new SecureRandom();
            result = new BigInteger(16, random).toString(10);
        }

        return result;
    }

    public static String getImageByBase64(String path) throws IOException {
        String result = null;
        File file = new File(path);

        // Reading a Image file from file system
        FileInputStream imageInFile = new FileInputStream(file);
        byte imageData[] = new byte[(int) file.length()];
        imageInFile.read(imageData);

        // Converting Image byte array into Base64 String
        System.out.println(encodeImage(imageData));
        result = encodeImage(imageData);
        imageInFile.close();

        return result;
    }

    public static String getImageByBase64(byte[] imageDate) {
        String result = null;
        result = encodeImage(imageDate);

        return result;
    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
