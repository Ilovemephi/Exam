
package mephi.b22901.ae.exam.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.Request;

/**
 *
 * @author artyom_egorkin
 */
public class RequestDAO {
    
    public int addRequest(Request request) {
        String sql = "INSERT INTO Requests (client_id, reason, status, diagnostic_result, master_id, work_result) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING request_id";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, request.getClientId());
            stmt.setString(2, request.getReason());
            stmt.setString(3, request.getStatus());
            stmt.setString(4, request.getDiagnosticResult());
            // master_id может быть null, если еще не назначены:
            if (request.getMasterId() != null) {
                stmt.setInt(5, request.getMasterId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setString(6, request.getWorkResult());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("request_id");
            } else {
                throw new SQLException("Не удалось получить id новой заявки");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка добавления заявки", e);
        }
    }
    
    public Request getRequestById(int requestId) {
        String sql = "SELECT request_id, client_id, reason, status, diagnostic_result, master_id, work_result " +
                     "FROM Requests WHERE request_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Request(
                        rs.getInt("request_id"),
                        rs.getInt("client_id"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("diagnostic_result"),
                        // для nullable FK используем getObject с Integer.class
                        (Integer)rs.getObject("master_id", Integer.class),
                        rs.getString("work_result")
                    );
                } else {
                    return null; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении заявки по id", e);
        }
    }
    
    
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT request_id, client_id, reason, status, diagnostic_result, master_id, work_result FROM Requests";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Request request = new Request(
                    rs.getInt("request_id"),
                    rs.getInt("client_id"),
                    rs.getString("reason"),
                    rs.getString("status"),
                    rs.getString("diagnostic_result"),
                    (Integer) rs.getObject("master_id", Integer.class),
                    rs.getString("work_result")
                );
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении всех заявок", e);
        }
        return requests;
    }
    
    public List<Request> getRequestsByClientId(int clientId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT request_id, client_id, reason, status, diagnostic_result, master_id, work_result " +
                     "FROM Requests WHERE client_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request(
                        rs.getInt("request_id"),
                        rs.getInt("client_id"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("diagnostic_result"),
                        (Integer) rs.getObject("master_id", Integer.class),
                        rs.getString("work_result")
                    );
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при поиске заявок по client_id", e);
        }
        return requests;
    }
    
    public boolean updateRequest(Request request) {
        String sql = "UPDATE Requests SET " + "reason = ?, " +"status = ?, " +
                     "diagnostic_result = ?, " +  "master_id = ?, "  +
                "work_result = ? " +  "WHERE request_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, request.getReason());
            stmt.setString(2, request.getStatus());
            stmt.setString(3, request.getDiagnosticResult());
            if (request.getMasterId() != null) {
                stmt.setInt(4, request.getMasterId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setString(5, request.getWorkResult());
            stmt.setInt(6, request.getRequestId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обновлении заявки", e);
        }
    }



}
