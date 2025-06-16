package mephi.b22901.ae.exam.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.RequestMechanics;

/**
 * Класс Data Access Object (DAO) для работы с таблицей связей между заявками и автослесарями
 * в базе данных (`Request_Mechanics`). Предоставляет методы для добавления автослесарей к заявкам
 * и получения списка автослесарей, связанных с конкретной заявкой.
 * @author artyom_egorkin
 */
public class RequestMechanicsDAO {
    
    
    /**
     * Добавляет автослесаря к указанной заявке в таблице `Request_Mechanics`.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @param employeeId Идентификатор сотрудника (автослесаря, уникальный ключ в таблице Employees).
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    /**
     * Возвращает список идентификаторов автослесарей, связанных с указанной заявкой.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Список целочисленных идентификаторов автослесарей ({@code List<Integer>}),
     *         связанных с данной заявкой. Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         
     */
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
