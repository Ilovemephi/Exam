/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, отвечающий за подключение к БД
 * @author artyom_egorkin
 */
public class DBConnection {
    private static final String URL = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.idxjorycpptjdgjuyjtf";
    private static final String PASSWORD = "Brateevo11b";
    
    private static Connection testConnection = null;

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Подключение к БД успешно");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД", e);
        }
    }
    
    
    public static void setTestConnection(Connection conn) {
        testConnection = conn;
    }
    
    
    
}