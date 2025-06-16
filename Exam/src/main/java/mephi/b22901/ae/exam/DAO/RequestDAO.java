
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
 * Класс Data Access Object (DAO) для работы с таблицей заявок в базе данных (`Requests`).
 * Предоставляет методы для создания, получения, обновления и поиска заявок по различным критериям.
 * @author artyom_egorkin
 */
public class RequestDAO {
    
    
    /**
     * Добавляет новую заявку в таблицу `Requests` и возвращает её идентификатор.
     * Использует SQL-выражение `RETURNING` для получения сгенерированного `request_id`.
     *
     * @param request Объект {@link Request}, содержащий данные новой заявки.
     *                Поле `master_id` может быть {@code null}, если мастер не назначен.
     * @return Идентификатор новой заявки (`request_id`).
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    /**
     * Возвращает заявку по её уникальному идентификатору.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Объект {@link Request}, соответствующий указанному идентификатору,
     *         или {@code null}, если заявка не найдена.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    
    /**
     * Возвращает список всех заявок, хранящихся в таблице `Requests`.
     *
     * @return Список объектов {@link Request}, представляющих все заявки.
     *         Возвращает пустой список, если заявок нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    
    /**
     * Возвращает список всех заявок, связанных с указанным идентификатором клиента.
     *
     * @param clientId Идентификатор клиента (уникальный ключ в таблице Clients).
     * @return Список объектов {@link Request}, соответствующих указанному клиенту.
     *         Возвращает пустой список, если заявок нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    /**
     * Обновляет данные существующей заявки в таблице `Requests`.
     *
     * @param request Объект {@link Request}, содержащий обновлённые данные.
     *                Поле `master_id` может быть {@code null}, если мастер не назначен.
     * @return {@code true}, если обновление прошло успешно (хотя бы одна строка изменена),
     *         {@code false} в противном случае.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
