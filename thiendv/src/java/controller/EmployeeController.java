/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Department;
import entity.Employee;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import model.DepartmentModel;
import model.EmployeeModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author doanvanthien
 */
@ManagedBean
@ViewScoped
public class EmployeeController implements Serializable {

    private static final String path = "uploadFile";

    public static enum PersistenceAction {
        SELECT,
        CREATE,
        UPDATE,
        DELETE
    }
    // variables
    private Employee memployee;
    private EmployeeModel memEmployeeModel;
    private List<Employee> mlistEmployee;
    private boolean isLogged;
    private PersistenceAction status;
    private List<Department> mlistDepartment;
    private DepartmentModel mdepartmentModel;
    private UploadedFile file;
    private List<Employee> mlistEmployeeSelected;
    

    // controller
    public EmployeeController() {
        try {
            memEmployeeModel = new EmployeeModel();
            memployee = new Employee();
            isLogged = checkIsLogged();
            mlistEmployee = memEmployeeModel.getAll();
            status = PersistenceAction.SELECT;
            mdepartmentModel = new DepartmentModel();
            mlistDepartment = mdepartmentModel.getAll();
            mlistEmployeeSelected = new ArrayList<>();

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addEmployee(ActionEvent event) {
        status = PersistenceAction.CREATE;
        memployee = new Employee();
        file = new DefaultUploadedFile();
        System.out.println("add");
    }

    public void backEmployee(ActionEvent event) {
        status = PersistenceAction.SELECT;
        memployee = new Employee();
        file = new DefaultUploadedFile();
        System.out.println("back");
    }

    public void preEdit(Employee employee) {
        this.memployee = employee;
        status = PersistenceAction.UPDATE;
    }

    public void preDelte(Employee employee) {
        this.memployee = employee;
        status = PersistenceAction.DELETE;
    }

    public void deleteEmployee(ActionEvent event) {
        if (status == PersistenceAction.DELETE) {
            try {
                if (memEmployeeModel.deleteEmployee(memployee.getId()) > 0) {
                    JsfUtil.addSuccessMessage("Delete employee successfully");
                    int index = 0;
                    for (Employee employee : mlistEmployee) {
                        if (employee.getId() == memployee.getId()) {
                            break;
                        } else {
                            index++;
                        }
                    }
                    mlistEmployee.remove(index);
                    file = new DefaultUploadedFile();
                    status = PersistenceAction.SELECT;
                    memployee = new Employee();
                } else {
                    JsfUtil.addErrorMessage("Occurred when delete employee");
                }
            } catch (IOException | ClassNotFoundException | SQLException ex) {
                JsfUtil.addErrorMessage(ex.toString());
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void preDeleteAll(ActionEvent event) {
        status = PersistenceAction.DELETE;
    }

    public void removeDeleteAll(ActionEvent event) {
        status = PersistenceAction.SELECT;
    }
    
    public void deleteAllEmployee(ActionEvent event) {
        if (status == PersistenceAction.DELETE) {
            try {
                if (memEmployeeModel.delelteAllEmployee(mlistEmployeeSelected) > 0) {
                    JsfUtil.addSuccessMessage("Delete all select employee successfully");
                    status = PersistenceAction.SELECT;
                    mlistEmployeeSelected = new ArrayList<>();
                    mlistEmployee = memEmployeeModel.getAll();
                }
            } catch (IOException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void save(ActionEvent event) {
        try {
            System.out.println(memployee.getEmail());
            if (status == PersistenceAction.CREATE) {

                if (memEmployeeModel.addEmployee(memployee) > 0) {
                    JsfUtil.addSuccessMessage("Add employee successfully");
                    mlistEmployee = memEmployeeModel.getAll();
                    file = new DefaultUploadedFile();
                    status = PersistenceAction.SELECT;
                    memployee = new Employee();
                } else {
                    JsfUtil.addErrorMessage("Occurred when create employee");

                }

            }

            if (status == PersistenceAction.UPDATE) {
                if (memEmployeeModel.updateEmployee(memployee) > 0) {
                    status = PersistenceAction.SELECT;
                    memployee = new Employee();
                    JsfUtil.addSuccessMessage("Update info employee successfully");
                    mlistEmployee = memEmployeeModel.getAll();
                } else {
                    JsfUtil.addErrorMessage("Occurred when update info employee");
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {

            JsfUtil.addErrorMessage(ex.toString());
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void detailEmployee(Employee employee) {
        this.memployee = employee;
        System.out.println("detail");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public void checkPhoneNumber(AjaxBehaviorEvent event) {
        if( ! JsfUtil.isNumeric(memployee.getCellPhone()) ) {
            JsfUtil.addErrorMessage("Num");
        }
    }
    
    private void createFolder(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    // getter and setter
    public Employee getMemployee() {
        return memployee;
    }

    public void setMemployee(Employee memployee) {
        this.memployee = memployee;
    }

    public List<Employee> getMlistEmployee() {
        return mlistEmployee;
    }

    public void setMlistEmployee(List<Employee> mlistEmployee) {
        this.mlistEmployee = mlistEmployee;
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;

    }

    public PersistenceAction getStatus() {
        return status;
    }

    public void setStatus(PersistenceAction status) {
        this.status = status;
    }

    public List<Department> getMlistDepartment() {
        return mlistDepartment;
    }

    public void setMlistDepartment(List<Department> mlistDepartment) {
        this.mlistDepartment = mlistDepartment;
    }

    public List<Employee> getMlistEmployeeSelected() {
        return mlistEmployeeSelected;
    }

    public void setMlistEmployeeSelected(List<Employee> mlistEmployeeSelected) {
        this.mlistEmployeeSelected = mlistEmployeeSelected;
    }

}
