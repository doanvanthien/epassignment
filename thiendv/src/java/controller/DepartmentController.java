/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Department;
import entity.Departments;
import entity.Employee;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import model.DepartmentModel;
import model.EmployeeModel;
import util.JsfUtil;

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
    private boolean isLogged;
    private List<Department> mlistDepartment;
    private PersistenceAction status;
    private List<Employee> mlistEmployee;
    private List<Employee> mlistEmployeeNotBelongDepartment;
    private List<Employee> mlistEmployeeBelongDepartment;
    private Departments mdepartments;
    private List<Departments> mlistDepartments;

    public static enum PersistenceAction {
        SELECT,
        CREATE,
        UPDATE,
        DELETE
    }

    // constructor
    public DepartmentController() {
        try {
            mdepartment = new Department();
            mdepartmentModel = new DepartmentModel();
            isLogged = checkIsLogged();
            mlistDepartment = mdepartmentModel.getAll();
            status = PersistenceAction.SELECT;
            EmployeeModel employeeModel = new EmployeeModel();
            mlistEmployeeNotBelongDepartment = employeeModel.getAllByDepartment(0);
            mlistEmployee = employeeModel.getAll();
            mlistDepartments = new ArrayList<>();
            setDepartments(mlistEmployee, mlistDepartment, mlistDepartments);
            System.out.println("ahihi");
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //methods
    private void setDepartments(List<Employee> employees, List<Department> departments, List<Departments> departmentss) {
        for (Department d : departments) {
            if (d.getEmployeeId() != 0) {
                for (Employee e : employees) {
                    if (d.getEmployeeId() == e.getId()) {
                        Departments ds = new Departments();
                        ds.setMdepartment(d);
                        ds.setNameEmployeeManager(e.getName());
                        departmentss.add(ds);
                    }
                }
            } else {
                Departments ds = new Departments();
                ds.setMdepartment(d);
                ds.setNameEmployeeManager(" ");
                departmentss.add(ds);
            }
        }
    }

    public void addDepartment(ActionEvent event) {
        mdepartment = new Department();
        status = PersistenceAction.CREATE;
    }

    public void backDepartment(ActionEvent event) {
        status = PersistenceAction.SELECT;
    }

    public void preEditDepartment(Department department) {
        mdepartment = department;
        status = PersistenceAction.UPDATE;
    }

    public void save(ActionEvent event) {
        try {
            if (status == PersistenceAction.CREATE) {

                if (mdepartmentModel.add(mdepartment) > 0) {
                    status = PersistenceAction.SELECT;
                    mdepartment = new Department();
                    mlistDepartment = mdepartmentModel.getAll();
                    mlistDepartments = new ArrayList<>();
                    setDepartments(mlistEmployee, mlistDepartment, mlistDepartments);
                    JsfUtil.addSuccessMessage("Add department successfully");
                } else {
                    JsfUtil.addErrorMessage("Error occurred when create department");

                }

            } else if (status == PersistenceAction.UPDATE) {
                if (mdepartmentModel.update(mdepartment) > 0) {
                    status = PersistenceAction.SELECT;
                    mdepartment = new Department();
                    mlistDepartment = mdepartmentModel.getAll();
                    mlistDepartments = new ArrayList<>();
                    setDepartments(mlistEmployee, mlistDepartment, mlistDepartments);
                    JsfUtil.addSuccessMessage("Update info department successfully");
                } else {
                    JsfUtil.addErrorMessage("Error occurred when update department");
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkIsLogged() {
        boolean result = false;
        HttpSession session = JsfUtil.getSession();
        User muser = (User) session.getAttribute("user");
        if (muser != null) {
            if (muser.getEmail() == null || muser.getUsername() == null) {
                result = true;
            }
        }

        return result;
    }

    public void preDelete(Department department) {
        mdepartment = department;
        status = PersistenceAction.DELETE;
    }

    public void deleteDepartment(ActionEvent event) {
        if (status == PersistenceAction.DELETE) {
            try {
                if (mdepartmentModel.delete(mdepartment.getId()) > 0) {
                    status = PersistenceAction.SELECT;
                    mdepartment = new Department();
                    mlistDepartment = mdepartmentModel.getAll();
                    JsfUtil.addSuccessMessage("Delete partment successfully");
                    mlistDepartments = new ArrayList<>();
                    setDepartments(mlistEmployee, mlistDepartment, mlistDepartments);
                } else {
                    JsfUtil.addErrorMessage("Error occurred when delete department");
                }
            } catch (IOException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void detailDepartment(Department department) {
        mdepartments = new Departments();
        mdepartments.setMdepartment(department);
        for (Departments ds : mlistDepartments) {
            if (ds.getMdepartment().getId() == department.getId()) {
                mdepartments = ds;
                break;
            }
        }

    }

    public void detailListEmployeeBelongDepartment(Department department) {
        try {
            EmployeeModel employeeModel = new EmployeeModel();
            mlistEmployeeBelongDepartment = employeeModel.getAllByDepartment(department.getId());
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // getter and setter
    public Department getMdepartment() {
        return mdepartment;
    }

    public void setMdepartment(Department mdepartment) {
        this.mdepartment = mdepartment;
    }

    public boolean isIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public List<Department> getMlistDepartment() {
        return mlistDepartment;
    }

    public void setMlistDepartment(List<Department> mlistDepartment) {
        this.mlistDepartment = mlistDepartment;
    }

    public PersistenceAction getStatus() {
        return status;
    }

    public void setStatus(PersistenceAction status) {
        this.status = status;
    }

    public List<Employee> getMlistEmployeeNotBelongDepartment() {
        return mlistEmployeeNotBelongDepartment;
    }

    public void setMlistEmployeeNotBelongDepartment(List<Employee> mlistEmployeeNotBelongDepartment) {
        this.mlistEmployeeNotBelongDepartment = mlistEmployeeNotBelongDepartment;
    }

    public List<Employee> getMlistEmployeeBelongDepartment() {
        return mlistEmployeeBelongDepartment;
    }

    public void setMlistEmployeeBelongDepartment(List<Employee> mlistEmployeeBelongDepartment) {
        this.mlistEmployeeBelongDepartment = mlistEmployeeBelongDepartment;
    }

    public List<Departments> getMlistDepartments() {
        return mlistDepartments;
    }

    public void setMlistDepartments(List<Departments> mlistDepartments) {
        this.mlistDepartments = mlistDepartments;
    }

    public Departments getMdepartments() {
        return mdepartments;
    }

    public void setMdepartments(Departments mdepartments) {
        this.mdepartments = mdepartments;
    }

}
