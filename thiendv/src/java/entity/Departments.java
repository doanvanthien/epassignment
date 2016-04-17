/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author doanvanthien
 */
public class Departments implements Serializable {

    private Department mdepartment;
    private String nameEmployeeManager;

    public Departments() {
        mdepartment = new Department();
    }

    public Department getMdepartment() {
        return mdepartment;
    }

    public void setMdepartment(Department mdepartment) {
        this.mdepartment = mdepartment;
    }

    public String getNameEmployeeManager() {
        return nameEmployeeManager;
    }

    public void setNameEmployeeManager(String nameEmployeeManager) {
        this.nameEmployeeManager = nameEmployeeManager;
    }

}
