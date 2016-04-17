/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Department;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author doanvanthien
 */
public class DepartmentModel extends ConnectionPool implements Serializable {

    public DepartmentModel() throws IOException, ClassNotFoundException, SQLException {
    }

    public List<Department> getAll() throws IOException, ClassNotFoundException, SQLException {
        List<Department> result = new ArrayList<>();
        String strSql = " select * from department";
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mresultSet = mpreparedStatement.executeQuery();
        while (mresultSet.next()) {
            Department department = new Department();
            department.setId(mresultSet.getInt("id"));
            department.setDepartmentCode(mresultSet.getString("department_code"));
            department.setName(mresultSet.getString("name"));
            department.setOfficePhone(mresultSet.getString("office_phone"));
            department.setEmployeeId(mresultSet.getInt("employee_id"));
            result.add(department);
        }

        closeAll();
        return result;
    }

    public int add(Department department) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        String strSql = "insert into department(department_code,name,office_phone,employee_id) values(?,?,?,?)";
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, department.getDepartmentCode());
        mpreparedStatement.setString(2, department.getName());
        mpreparedStatement.setString(3, department.getOfficePhone());
        mpreparedStatement.setInt(4, department.getEmployeeId());
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

    public int update(Department department) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        String strSql = "update department set department_code = ? , name = ? , office_phone = ? ,employee_id = ? where id = " + department.getId();
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        mpreparedStatement.setString(1, department.getDepartmentCode());
        mpreparedStatement.setString(2, department.getName());
        mpreparedStatement.setString(3, department.getOfficePhone());
        mpreparedStatement.setInt(4, department.getEmployeeId());
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

    public int delete(int id) throws IOException, ClassNotFoundException, SQLException {
        int result = -1;
        String strSql = "delete department where id = " + id;
        openConnection();
        mpreparedStatement = connection.prepareStatement(strSql);
        result = mpreparedStatement.executeUpdate();

        closeAll();
        return result;
    }

}
