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
import mephi.b22901.ae.exam.RequestService;
import mephi.b22901.ae.exam.Service;

/**
 *DAO Класс для заявок, который отвечает за работу с таблицей Service в БД 
 * Предоставляет методы получения всех услуг, услуги по ее ID, по ее категории 
 * @author artyom_egorkin
 */
public class ServiceDAO {
    
    
    
    
    /**
     * Возвращает список всех услуг, доступных в базе данных.
     *
     * @return Список объектов {@link Service}, представляющих все услуги.
     *         Возвращает пустой список, если услуг нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
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
    
    
    
    
    /**
     * Возвращает услугу по её уникальному идентификатору.
     *
     * @param serviceId Идентификатор услуги (уникальный ключ в таблице Services).
     * @return Объект {@link Service}, соответствующий указанному идентификатору,
     *         или {@code null}, если услуга не найдена.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
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
    
    
    
    /**
     * Возвращает первую услугу, соответствующую указанной категории.
     * Если несколько услуг имеют одну категорию, возвращается только первая запись.
     *
     * @param category Категория услуги для поиска (например, "Диагностика").
     * @return Объект {@link Service}, соответствующий указанной категории,
     *         или {@code null}, если услуга не найдена.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
    public Service getServiceByCategory(String category) {
         String sql = "SELECT service_id, category, subcategory, price, required_mechanic_role FROM Services WHERE category = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, category);
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
