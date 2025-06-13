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
import mephi.b22901.ae.exam.RequestPart;

/**
 *
 * @author artyom_egorkin
 */
public class RequestPartDAO {
    // Добавить связь между заявкой и деталью
    public void addRequestPart(RequestPart requestPart) {
        String sql = "INSERT INTO Request_Parts (request_id, part_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestPart.getRequestId());
            stmt.setInt(2, requestPart.getPartId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при добавлении детали к заявке", e);
        }
    }

    // Получить список id деталей по заявке
    public List<Integer> getPartIdsByRequestId(int requestId) {
        List<Integer> partIds = new ArrayList<>();
        String sql = "SELECT part_id FROM Request_Parts WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                partIds.add(rs.getInt("part_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении деталей для заявки", e);
        }
        return partIds;
    }

    // Получить список id заявок по детали
    public List<Integer> getRequestIdsByPartId(int partId) {
        List<Integer> requestIds = new ArrayList<>();
        String sql = "SELECT request_id FROM Request_Parts WHERE part_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, partId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requestIds.add(rs.getInt("request_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении заявок для детали", e);
        }
        return requestIds;
    }


}
