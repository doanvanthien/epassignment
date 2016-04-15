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
            department.setName(mresultSet.getString("name"));
            department.setOfficePhone(mresultSet.getString("office_phone"));
            department.setEmployeeId(mresultSet.getInt("empoyee_id"));
            result.add(department);
        }
        
        closeAll();
        return result;
    }
}
