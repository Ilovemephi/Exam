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
import mephi.b22901.ae.exam.Service;

/**
 *
 * @author artyom_egorkin
 */
public class ServiceDAO {
    
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT service_id, category, subcategory, price, required_mechanic_role FROM Services";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("service_id"),
                        rs.getString("category"),
                        rs.getString("subcategory"),
                        rs.getDouble("price"),
                        rs.getString("required_mechanic_role")
                );
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении списка услуг", e);
        }
        return services;
    }
    
    public Service getServiceById(int serviceId) {
        String sql = "SELECT service_id, category, subcategory, price, required_mechanic_role FROM Services WHERE service_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, serviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Service(
                            rs.getInt("service_id"),
                            rs.getString("category"),
                            rs.getString("subcategory"),
                            rs.getDouble("price"),
                            rs.getString("required_mechanic_role")
                    );
                } else {
                    return null; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении услуги по id", e);
        }
    }

    
    

}
