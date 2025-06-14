/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.Connection.DBConnection;

/**
 *
 * @author artyom_egorkin
 */
public class ClientDAO {
    
    public int addClient(Client client) {
        String sql = "INSERT INTO Clients (full_name, phone_number, car_number) VALUES (?, ?, ?) RETURNING client_id";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, client.getFullName());
            stmt.setString(2, client.getPhoneNumber());
            stmt.setString(3, client.getCarNumber());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("client_id"); 
            } else {
                throw new SQLException("Не удалось получить id нового клиента");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка добавления клиента", e);
        }
    }
    
    public Client getClientById(int id) {
        String sql = "SELECT * FROM Clients WHERE client_id = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id); // Передаем id вместо ?
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("client_id"), rs.getString("full_name"), rs.getString("phone_number"),rs.getString("car_number"));
            } else {
                return null; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения клиента по id", e);
        }
    }
    
    
    public Client getClientByPhone(String phone) {
        String sql = "SELECT * FROM Clients WHERE phone_number = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, phone);      // Подставляем телефон вместо ?
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("client_id"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    rs.getString("car_number")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка поиска клиента по номеру телефона", e);
        }
    }
    
    
    public Client getClientByCarNumber(String carNumber) {
        String sql = "SELECT * FROM Clients WHERE car_number = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, carNumber);   
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("client_id"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    rs.getString("car_number")
                );
            } else {
                return null; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка поиска клиента по номеру авто", e);
        }
    }
    
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Client client = new Client(
                    rs.getInt("client_id"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    rs.getString("car_number")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения списка клиентов", e);
        }
        return clients;
    }
    
    public boolean updateClient(Client client) {
        String sql = "UPDATE Clients SET full_name = ?, phone_number = ?, car_number = ? WHERE client_id = ?";
        try ( Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getFullName());
            stmt.setString(2, client.getPhoneNumber());
            stmt.setString(3, client.getCarNumber());
            stmt.setInt(4, client.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обновлении данных клиента", e);
        }
    }

    
}
