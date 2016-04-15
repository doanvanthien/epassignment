/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import model.*;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import util.JsfUtil;
import util.MailServer;

/**
 *
 * @author thiendv
 */
@ManagedBean
@ViewScoped
public class UserController implements Serializable {

    private User muser;
    private User mcreateUser;
    private UserModel muserModel;
    private boolean isLogin;
    private Password mpassword;
    private boolean inputChangePasswordCorrect;
    private List<User> musers;

    public UserController() {
        try {
            muserModel = new UserModel();
            isLogin = false;
            inputChangePasswordCorrect = false;
            mpassword = new Password();
            HttpSession session = JsfUtil.getSession();
            muser = (User) session.getAttribute("user");
            if (muser != null) {
                muser = muserModel.getUser(muser.getUsername());
                if (muser.getUsername() != null) {
                    isLogin = true;
                }
            } else {
                muser = new User();
            }
            mcreateUser = new User();
            musers = muserModel.getAllUser();
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String login(ActionEvent event) {
        try {

            if (muserModel.validate(muser.getUsername(), muser.getPassword())) {
                JsfUtil.setSessionValue("user", muser);
                muser = muserModel.getUser(muser.getUsername());
                isLogin = true;

            } else {
                muser = new User();
                isLogin = false;
                JsfUtil.addErrorMessage("Please enter correct username and password");
            }

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "login";
    }

    public String logout(ActionEvent event) {
        JsfUtil.setSessionValue("user", null);
        JsfUtil.removeSession("user");
        isLogin = false;
        //JsfUtil.navigate("logout"); //
        return "logout";
    }

    /**
     * Function for change password
     *
     * @param event
     */
    public void checkOldPassword(AjaxBehaviorEvent event) {
        try {
            String passwordMD5 = JsfUtil.convertToMD5(mpassword.getCurrentPassword());
            System.out.println(passwordMD5 + "\n" + muser.getPassword());
            if (passwordMD5 == null || muser.getPassword().compareTo(passwordMD5) != 0) {
                JsfUtil.addErrorMessage("Mật khẩu cũ không đúng");
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkNewPassword(AjaxBehaviorEvent event) {

        if (mpassword.getNewPassword().compareTo(mpassword.getRepeatNewPassword()) != 0) {
            JsfUtil.addErrorMessage("Mật khẩu mới không trùng với mật khẩu nhập lại");
        } else {
            inputChangePasswordCorrect = true;
        }

    }

    public void saveChangePassword(ActionEvent event) {
        try {
            if (muserModel.changePassword(mpassword.getNewPassword(), muser.getId()) > 0) {
                JsfUtil.addSuccessMessage("Change password successfully");
                inputChangePasswordCorrect = false;
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            JsfUtil.addErrorMessage("error: \n" + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create account
     *
     * @param event
     */
    public void checkUser(AjaxBehaviorEvent event) {
        int i = 0;
        for (User user : musers) {
            if (mcreateUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                JsfUtil.addErrorMessage("Username have already");
                mcreateUser.setUsername(null);
                break;
            } else {
                i++;
            }
        }
        if (i == musers.size()) {
            JsfUtil.addSuccessMessage("Username can be used");
        }
    }

    public void checkEmail(AjaxBehaviorEvent event) {
        int i = 0;
        for (User user : musers) {
            if (mcreateUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                JsfUtil.addErrorMessage("Email have already");
                mcreateUser.setEmail(null);
                break;
            } else {
                i++;
            }
        }
        if (i == musers.size()) {
            JsfUtil.addSuccessMessage("Email can be used");
        }
    }

    public void createAccount(ActionEvent event) {
        try {
            String password = JsfUtil.randomPassword();
            String toEmail = mcreateUser.getEmail();
            String subject = "CONFIRM ACCOUNT FROM SYSTEM VNSOFTWARE";
            String body = "Hi " + toEmail + ". \n"
                    + " \n username: " + mcreateUser.getUsername() + " \n "
                    + " \n password: " + password + ""
                    + "\n \n \n"
                    + "PLEASE. CHANGE PASSWORD ON FIRST LOGIN \n \n \n"
                    + "admin: thiendv";
            mcreateUser.setPassword(password);
            MailServer.sendMessages(toEmail, subject, body);
            if (muserModel.addUser(mcreateUser) > 0) {
                JsfUtil.addSuccessMessage("Create account successfully.");
            }
        } catch (IOException | ClassNotFoundException | SQLException | NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
            JsfUtil.addErrorMessage("Error \n " + ex.toString());
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }
    }

    // getter and setter
    public User getMuser() {
        return muser;
    }

    public void setMuser(User muser) {
        this.muser = muser;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Password getMpassword() {
        return mpassword;
    }

    public void setMpassword(Password mpassword) {
        this.mpassword = mpassword;
    }

    public boolean isInputChangePasswordCorrect() {
        return inputChangePasswordCorrect;
    }

    public void setInputChangePasswordCorrect(boolean inputChangePasswordCorrect) {
        this.inputChangePasswordCorrect = inputChangePasswordCorrect;
    }

    public User getMcreateUser() {
        return mcreateUser;
    }

    public void setMcreateUser(User mcreateUser) {
        this.mcreateUser = mcreateUser;
    }

}
