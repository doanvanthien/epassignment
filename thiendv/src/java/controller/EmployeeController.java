/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Department;
import entity.Employee;
import entity.Employees;
import entity.User;
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
    private List<Employees> mlistEmployeesSelected;
    private Employees memployees;
    private List<Employees> mlistEmployees;

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
            mlistEmployeesSelected = new ArrayList<>();
            memployees = new Employees();
            mlistEmployees = new ArrayList<>();
            setEmployees(mlistDepartment, mlistEmployee, mlistEmployees);
            System.out.println("ahihi");

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setEmployees(List<Department> departments, List<Employee> employees, List<Employees> employeess) {
        for (Employee e : employees) {
            if (e.getDepartmentId() != 0) {
                for (Department d : departments) {
                    if (d.getEmployeeId() == e.getId()) {
                        Employees es = new Employees();
                        es.setEmployee(e);
                        es.setNameDepartment(d.getName());
                        employeess.add(es);
                    }
                }
            } else {
                Employees es = new Employees();
                es.setEmployee(e);
                es.setNameDepartment(" ");
                employeess.add(es);
            }
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
        memployee = employee;
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
                    mlistEmployees = new ArrayList<>();
                    setEmployees(mlistDepartment, mlistEmployee, mlistEmployees);
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
                if (memEmployeeModel.delelteAllEmployee(mlistEmployeesSelected) > 0) {
                    JsfUtil.addSuccessMessage("Delete all select employee successfully");
                    status = PersistenceAction.SELECT;
                    mlistEmployeesSelected = new ArrayList<>();
                    mlistEmployee = memEmployeeModel.getAll();
                    mlistEmployees = new ArrayList<>();
                    setEmployees(mlistDepartment, mlistEmployee, mlistEmployees);
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
                    mlistEmployees = new ArrayList<>();
                    setEmployees(mlistDepartment, mlistEmployee, mlistEmployees);
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
                    mlistEmployees = new ArrayList<>();
                    setEmployees(mlistDepartment, mlistEmployee, mlistEmployees);
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
        memployees = new Employees();
        memployees.setEmployee(employee);
        for (Department department : mlistDepartment) {
            if (department.getId() == employee.getDepartmentId()) {
                memployees.setNameDepartment(department.getName());
                break;
            }
        }
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
        if (!JsfUtil.isNumeric(memployee.getCellPhone())) {
            JsfUtil.addErrorMessage("Num");
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

    public List<Employees> getMlistEmployeesSelected() {
        return mlistEmployeesSelected;
    }

    public void setMlistEmployeesSelected(List<Employees> mlistEmployeesSelected) {
        this.mlistEmployeesSelected = mlistEmployeesSelected;
    }

    public Employees getMemployees() {
        return memployees;
    }

    public void setMemployees(Employees memployees) {
        this.memployees = memployees;
    }

    public List<Employees> getMlistEmployees() {
        return mlistEmployees;
    }

    public void setMlistEmployees(List<Employees> mlistEmployees) {
        this.mlistEmployees = mlistEmployees;
    }

}
