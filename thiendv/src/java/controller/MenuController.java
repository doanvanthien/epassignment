/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author doanvanthien
 */
@ManagedBean
@ViewScoped
public class MenuController implements Serializable {

    // declare variables
    private MenuModel menuModel;
    private String navigation;

    /**
     * METHOD CONTROLLER
     */
    public MenuController() {
        menuModel = new DefaultMenuModel();
        DefaultMenuItem itemHome = new DefaultMenuItem("Home");
        itemHome.setCommand("#{menuController.changeNavigation}");
        itemHome.setParam("url", "../admin/home.xhtml");
        itemHome.setIcon("ui-icon-star");
        itemHome.setOnclick("1");
        menuModel.addElement(itemHome);
        
        DefaultMenuItem itemEmployee = new DefaultMenuItem("Employee");
        itemEmployee.setCommand("#{menuController.changeNavigation}");
        itemEmployee.setParam("url", "../admin/employee.xhtml");
        itemEmployee.setIcon("ui-icon-star");
        itemEmployee.setOnclick("1");
        menuModel.addElement(itemEmployee);

        DefaultMenuItem itemDepartment = new DefaultMenuItem("Department");
        itemDepartment.setCommand("#{menuController.changeNavigation}");
        itemDepartment.setParam("url", "../admin/department.xhtml");
        itemDepartment.setIcon("ui-icon-heart");
        itemDepartment.setOnclick("2");
        menuModel.addElement(itemDepartment);
        navigation = "/admin/home.xhtml";
    }

    public void changeNavigation(MenuActionEvent event) {
        this.navigation = event.getMenuItem().getParams().get("url").get(0);
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("panel_main_form");
    }

    /**
     * METHODS SETTER AND GETTER
     *
     * @return
     */
    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

}
