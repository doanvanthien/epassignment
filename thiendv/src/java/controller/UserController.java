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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author thiendv
 */
@ManagedBean
@ViewScoped
public class UserController implements Serializable {

    private User muser;
    private UserModel muserModel;
    private boolean isLogin;
    private Password mpassword;

    public UserController() {
        try {
            muserModel = new UserModel();
            isLogin = false;
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

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login(ActionEvent event) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            boolean loggedIn = false;

            if (muserModel.validate(muser.getUsername(), muser.getPassword())) {
                loggedIn = true;
                JsfUtil.setSessionValue("user", muser);
                isLogin = true;
            } else {
                loggedIn = false;
                muser = new User();
                isLogin = false;
                JsfUtil.addErrorMessage("Please enter correct username and password");
            }
            context.addCallbackParam("loggedIn", loggedIn);
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void test(ActionEvent event) {
        System.out.println("here");

    }

    public void logout(ActionEvent event) {
        JsfUtil.setSessionValue("user", null);
        JsfUtil.removeSession("user");
        isLogin = false;
        //JsfUtil.navigate("logout");
    }

    /**
     * Function for change password
     *
     * @param event
     */
    public void checkOldPassword(AjaxBehaviorEvent event) {
        try {
            String password = muserModel.convertToMD5(mpassword.getCurrentPassword());
            if (password == null || muser.getPassword().compareTo(password) != 0) {
                JsfUtil.addErrorMessage("Mật khẩu cũ không đúng");
            }

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkNewPassword(AjaxBehaviorEvent event) {

        if (mpassword.getNewPassword().compareTo(mpassword.getRepeatNewPassword()) != 0) {
            JsfUtil.addErrorMessage("Mật khẩu mới không tr");
        }

    }

    public void saveChangePassword(ActionEvent event) {
        try {
            if (muserModel.changePassword(mpassword.getNewPassword(), muser.getId()) > 0) {
                JsfUtil.addSuccessMessage("success");
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            JsfUtil.addErrorMessage("error: \n" + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
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

}
