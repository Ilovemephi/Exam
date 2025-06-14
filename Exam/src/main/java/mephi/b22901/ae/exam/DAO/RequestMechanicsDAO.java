package mephi.b22901.ae.exam.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.RequestMechanics;

public class RequestMechanicsDAO {
    
    public void addMechanicToRequest(int requestId, int employeeId) {
        String sql = "INSERT INTO Request_Mechanics (request_id, employee_id) VALUES (?, ?)";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, employeeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при добавлении автослесаря к заявке", e);
        }
    }
    
    public List<Integer> getMechanicsForRequest(int requestId) {
        List<Integer> mechanicIds = new ArrayList<>();
        String sql = "SELECT employee_id FROM Request_Mechanics WHERE request_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mechanicIds.add(rs.getInt("employee_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении автослесарей для заявки", e);
        }
        return mechanicIds;
    }
}
