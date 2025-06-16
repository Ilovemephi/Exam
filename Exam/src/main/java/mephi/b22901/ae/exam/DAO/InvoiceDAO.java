/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.Invoice;

/**
 * Класс Data Access Object (DAO) для работы с таблицей счетов в базе данных (`Invoices`).
 * Предоставляет методы для создания новых счетов и получения существующих счетов по их идентификатору.
 * @author artyom_egorkin
 */
public class InvoiceDAO {
    
    
    
    /**
     * Добавляет новый счёт в таблицу `Invoices` и возвращает его идентификатор.
     * Использует SQL-выражение `RETURNING` для получения сгенерированного `invoice_id`.
     *
     * @param invoice Объект {@link Invoice}, содержащий данные нового счёта.
     * @return Идентификатор нового счёта (`invoice_id`).
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
    
    public int addInvoice(Invoice invoice) {
        String sql = "INSERT INTO Invoices (request_id, client_id, master_id, total_amount) VALUES (?, ?, ?, ?) RETURNING invoice_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoice.getRequestId());
            stmt.setInt(2, invoice.getClientId());
            stmt.setInt(3, invoice.getMasterId());
            stmt.setInt(4, invoice.getTotalAmount());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("invoice_id");
            } else {
                throw new SQLException("Не удалось получить id нового счета");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка добавления счета", e);
        }
    }

    
    
    /**
     * Возвращает счёт по его уникальному идентификатору.
     *
     * @param invoiceId Идентификатор счёта (уникальный ключ в таблице Invoices).
     * @return Объект {@link Invoice}, соответствующий указанному идентификатору,
     *         или {@code null}, если счёт не найден.
     * @throws RuntimeException Если возникает ошибка при выполнении SQL-запроса
     */
    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT invoice_id, request_id, client_id, master_id, total_amount FROM Invoices WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Invoice(
                    rs.getInt("invoice_id"),
                    rs.getInt("request_id"),
                    rs.getInt("client_id"),
                    rs.getInt("master_id"),
                    rs.getInt("total_amount")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении счета", e);
        }
    }


    
}
