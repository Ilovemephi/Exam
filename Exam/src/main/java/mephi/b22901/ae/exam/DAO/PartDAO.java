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
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.RequestPart;

/**
 * Класс Data Access Object (DAO) для работы с таблицей запчастей в базе данных (`Parts`).
 * Предоставляет методы для получения информации о запчастях, таких как получение всех запчастей
 * или запчасти по её идентификатору.
 *
 * @author artyom_egorkin
 */
public class PartDAO {
    
    
    /**
     * Возвращает список всех запчастей, хранящихся в таблице `Parts`.
     *
     * @return Список объектов {@link Part}, представляющих все запчасти.
     *         Возвращает пустой список, если запчастей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
    public List<Part> getAllParts() {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT part_id, name, category, subcategory, price FROM Parts";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Part part = new Part(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("subcategory"),
                    rs.getDouble("price")
                );
                parts.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении всех запчастей", e);
        }
        return parts;
    }
    
    
    
    /**
     * Возвращает запчасть по её уникальному идентификатору.
     *
     * @param partId Идентификатор запчасти (уникальный ключ в таблице Parts).
     * @return Объект {@link Part}, соответствующий указанному идентификатору,
     *         или {@code null}, если запчасть не найдена.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
    public Part getPartById(int partId) {
        String sql = "SELECT part_id, name, category, subcategory, price FROM Parts WHERE part_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, partId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Part(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("subcategory"),
                    rs.getDouble("price")      
                );
            } else {
                return null; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении детали по id", e);
        }
    }
    
    
    

    
    
}
