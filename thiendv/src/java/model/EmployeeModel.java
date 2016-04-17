/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author doanvanthien
 */
public class EmployeeModel extends ConnectionPool implements Serializable {

    public EmployeeModel() throws IOException, ClassNotFoundException, SQLException {
    }

    public List<Employee> getAll() throws IOException, ClassNotFoundException, SQLException {
        List<Employee> result = new ArrayList<>();
        String strSql = "select * from employee";
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            Employee employee = new Employee();
            employee.setId(mresultSet.getInt("id"));
            employee.setEmployeeCode(mresultSet.getString("employee_code"));
            employee.setName(mresultSet.getString("name"));
            employee.setEmail(mresultSet.getString("email"));
            employee.setCellPhone(mresultSet.getString("cell_phone"));
            employee.setJobTitle(mresultSet.getString("job_title"));
            employee.setPhoto(mresultSet.getString("photo"));
            employee.setDepartmentId(mresultSet.getInt("department_id"));
            result.add(employee);
        }
        closeAll();
        return result;
    }
    
    
    
    public List<Employee> getAllByDepartment(int department_id) throws IOException, ClassNotFoundException, SQLException {
        List<Employee> result = new ArrayList<>();
        String strSql = "select * from employee where department_id = " + department_id;
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            Employee employee = new Employee();
            employee.setId(mresultSet.getInt("id"));
            employee.setEmployeeCode(mresultSet.getString("employee_code"));
            employee.setName(mresultSet.getString("name"));
            employee.setEmail(mresultSet.getString("email"));
            employee.setCellPhone(mresultSet.getString("cell_phone"));
            employee.setJobTitle(mresultSet.getString("job_title"));
            employee.setPhoto(mresultSet.getString("photo"));
            employee.setDepartmentId(mresultSet.getInt("department_id"));
            result.add(employee);
        }
        closeAll();
        return result;
    }

    public int addEmployee(Employee employee) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        openConnection();
        String strSql = "insert into employee(name,email,cell_phone,job_title,photo,department_id,employee_code) values(?,?,?,?,?,?,?)";
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, employee.getName());
        mpreparedStatement.setString(2, employee.getEmail());
        mpreparedStatement.setString(3, employee.getCellPhone());
        mpreparedStatement.setString(4, employee.getJobTitle());
        mpreparedStatement.setString(5, employee.getPhoto());
        mpreparedStatement.setInt(6, employee.getDepartmentId());
        mpreparedStatement.setString(7, employee.getEmployeeCode());
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

    public int updateEmployee(Employee employee) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        openConnection();
        String strSql = "update employee set name = ?, email = ? , cell_phone = ?, job_title = ?,  photo = ? ,department_id = ? , employee_code = ? where id =  " + employee.getId();
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, employee.getName());
        mpreparedStatement.setString(2, employee.getEmail());
        mpreparedStatement.setString(3, employee.getCellPhone());
        mpreparedStatement.setString(4, employee.getJobTitle());
        mpreparedStatement.setString(5, employee.getPhoto());
        mpreparedStatement.setInt(6, employee.getDepartmentId());
        mpreparedStatement.setString(7, employee.getEmployeeCode());
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

    public int deleteEmployee(int id) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        openConnection();
        String strSql = "delete employee where id = " + id;
        mpreparedStatement = connection.prepareStatement(strSql);
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

    public int delelteAllEmployee(List<Employee> listEmployee) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        openConnection();
        for (Employee employee : listEmployee) {
            String strSql = "delete employee where id = " + employee.getId();
            mpreparedStatement = connection.prepareStatement(strSql);
            result = mpreparedStatement.executeUpdate();
        }

        closeAll();
        return result;
    }
}
