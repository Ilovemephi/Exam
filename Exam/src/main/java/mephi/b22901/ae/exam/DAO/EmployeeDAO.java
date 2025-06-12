/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.Employee;

/**
 *
 * @author artyom_egorkin
 */
public class EmployeeDAO {
    
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT employee_id, full_name, role FROM Employees WHERE employee_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("full_name"),
                    rs.getString("role")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения сотрудника по id", e);
        }
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, full_name, role FROM Employees";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("full_name"),
                    rs.getString("role")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения списка сотрудников", e);
        }
        return employees;
    }
    
    
    public List<Employee> getEmployeesByRole(String role) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, full_name, role FROM Employees WHERE role = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("full_name"),
                    rs.getString("role")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения сотрудников по роли", e);
        }
        return employees;
    }
    
    
}
