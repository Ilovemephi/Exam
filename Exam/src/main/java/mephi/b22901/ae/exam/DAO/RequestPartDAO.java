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
 * Класс Data Access Object (DAO) для работы с таблицей связей между заявками и деталями
 * в базе данных. Предоставляет методы для добавления деталей к заявкам,
 * а также получения идентификаторов деталей и заявок по их связям.
 *
 * @author artyom_egorkin
 */
public class RequestPartDAO {

    
    /**
     * Добавляет деталь к указанной заявке в таблице `Request_Parts`.
     *
     * @param requestPart Объект {@link RequestPart}, содержащий идентификаторы
     *                    заявки (`request_id`) и детали (`part_id`).
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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


    
    /**
     * Возвращает список идентификаторов деталей, связанных с указанной заявкой.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Список целочисленных идентификаторов деталей ({@code List<Integer>}),
     *         связанных с данной заявкой. Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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

   
    
    /**
     * Возвращает список идентификаторов заявок, связанных с указанной деталью.
     *
     * @param partId Идентификатор детали (уникальный ключ в таблице Parts).
     * @return Список целочисленных идентификаторов заявок ({@code List<Integer>}),
     *         связанных с данной деталью. Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
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
    
    
    
    /**
     * Возвращает список объектов {@link RequestPart}, представляющих детали,
     * связанные с указанной заявкой.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Список объектов {@link RequestPart}, содержащих пары
     *         (`request_id`, `part_id`). Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
    public List<RequestPart> getRequestPartsByRequestId(int requestId) {
        List<RequestPart> requestParts = new ArrayList<>();
        String sql = "SELECT request_id, part_id FROM Request_Parts WHERE request_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RequestPart requestPart = new RequestPart(
                        rs.getInt("request_id"),
                        rs.getInt("part_id")
                    );
                    requestParts.add(requestPart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении деталей заявки #" + requestId, e);
        }
        return requestParts;
    }


}
