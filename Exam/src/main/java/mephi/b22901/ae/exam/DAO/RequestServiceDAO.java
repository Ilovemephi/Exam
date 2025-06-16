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

/**
 * DAO Класс для работы с таблицей связей между заявками и ууслугами
 * в БД. Предоставляет методы для добавления услуг к заявкам,
 * а также получения идентификаторов услуг и заявок по их связям.
 * @author artyom_egorkin
 */
public class RequestServiceDAO {
    
    
    
    
    /**
     * Добавляет услугу к указанной заявке в таблице `Requests_Services`.
     *
     * @param requestService Объект {@link RequestService}, содержащий идентификаторы
     *                       заявки (`request_id`) и услуги (`service_id`).
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
    public void addRequestService(RequestService requestService) {
        String sql = "INSERT INTO Requests_Services (request_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestService.getRequestId());
            stmt.setInt(2, requestService.getServiceId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при добавлении услуги к заявке", e);
        }
    }

   
    
    /**
     * Возвращает список идентификаторов услуг, связанных с указанной заявкой.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Список целочисленных идентификаторов услуг ({@code List<Integer>}),
     *         связанных с данной заявкой. Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
    public List<Integer> getServiceIdsByRequestId(int requestId) {
        String sql = "SELECT service_id FROM Requests_Services WHERE request_id = ?";
        List<Integer> serviceIds = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                serviceIds.add(rs.getInt("service_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении id услуг по заявке", e);
        }
        return serviceIds;
    }

    /**
     * Возвращает список идентификаторов заявок, связанных с указанной услугой.
     *
     * @param serviceId Идентификатор услуги (уникальный ключ в таблице Services).
     * @return Список целочисленных идентификаторов заявок ({@code List<Integer>}),
     *         связанных с данной услугой. Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
    public List<Integer> getRequestIdsByServiceId(int serviceId) {
        String sql = "SELECT request_id FROM Requests_Services WHERE service_id = ?";
        List<Integer> requestIds = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requestIds.add(rs.getInt("request_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении id заявок по услуге", e);
        }
        return requestIds;
    }
    
    
    
    /**
     * Возвращает список объектов {@link RequestService}, представляющих услуги,
     * связанные с указанной заявкой.
     *
     * @param requestId Идентификатор заявки (уникальный ключ в таблице Requests).
     * @return Список объектов {@link RequestService}, содержащих пары
     *         (`request_id`, `service_id`). Возвращает пустой список, если связей нет.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     *         (обёртка над {@link SQLException}).
     */
    public List<RequestService> getRequestServicesByRequestId(int requestId) {
        List<RequestService> requestServices = new ArrayList<>();
        String sql = "SELECT request_id, service_id FROM Requests_Services WHERE request_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RequestService requestService = new RequestService(
                        rs.getInt("request_id"),
                        rs.getInt("service_id")
                    );
                    requestServices.add(requestService);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении услуг заявки #" + requestId, e);
        }
        return requestServices;
    }
    

    
}
