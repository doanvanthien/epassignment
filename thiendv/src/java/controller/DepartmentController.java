/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Department;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.DepartmentModel;

/**
 *
 * @author doanvanthien
 */
@ManagedBean
@ViewScoped
public class DepartmentController implements Serializable {

    // variables
    private Department mdepartment;
    private DepartmentModel mdepartmentModel;

    // constructor
    public DepartmentController() {
    }

    // getter and setter
    public Department getMdepartment() {
        return mdepartment;
    }

    public void setMdepartment(Department mdepartment) {
        this.mdepartment = mdepartment;
    }

}
