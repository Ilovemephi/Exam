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
 *
 * @author artyom_egorkin
 */
public class InvoiceDAO {
    
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
