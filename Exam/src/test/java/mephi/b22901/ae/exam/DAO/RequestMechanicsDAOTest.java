/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import org.junit.jupiter.api.*;
import mephi.b22901.ae.exam.DAO.RequestMechanicsDAO;
import mephi.b22901.ae.exam.Connection.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestMechanicsDAOTest {

    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    @BeforeAll
    public static void setUpBeforeAll() {
        // Переключаемся на H2 для тестов
        DBConnection.useUrl(H2_URL, H2_USER, H2_PASSWORD);
        // Инициализация таблицы Request_Mechanics
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Request_Mechanics");
            stmt.execute("CREATE TABLE Request_Mechanics (" +
                    "request_id INT, " +
                    "employee_id INT)");
            System.out.println("Таблица Request_Mechanics создана успешно");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при настройке тестовой БД", e);
        }
    }

    @AfterAll
    public static void tearDownAfterAll() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Request_Mechanics");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке тестовой БД", e);
        }
        
        DBConnection.useUrl("jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres",
                       "postgres.idxjorycpptjdgjuyjtf",
                       "Brateevo11b");
        System.out.println("Сброс подключения на PostgreSQL");
    }

    @BeforeEach
    public void setUp() {
        // Очистка таблицы перед каждым тестом
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Request_Mechanics");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }

    @Test
    public void testAddMechanicToRequest() {
        RequestMechanicsDAO requestMechanicsDAO = new RequestMechanicsDAO();
        requestMechanicsDAO.addMechanicToRequest(1, 101); // request_id = 1, employee_id = 101

      
        List<Integer> mechanicIds = requestMechanicsDAO.getMechanicsForRequest(1);
        assertNotNull(mechanicIds, "Список идентификаторов не должен быть null");
        assertEquals(1, mechanicIds.size(), "Должен быть 1 автослесарь");
        assertTrue(mechanicIds.contains(101), "ID автослесаря должен быть 101");
    }

    @Test
    public void testAddMultipleMechanicsToRequest() {
        RequestMechanicsDAO requestMechanicsDAO = new RequestMechanicsDAO();
        requestMechanicsDAO.addMechanicToRequest(2, 101); // request_id = 2, employee_id = 101
        requestMechanicsDAO.addMechanicToRequest(2, 102); // request_id = 2, employee_id = 102

        List<Integer> mechanicIds = requestMechanicsDAO.getMechanicsForRequest(2);
        assertNotNull(mechanicIds, "Список идентификаторов не должен быть null");
        assertEquals(2, mechanicIds.size(), "Должно быть 2 автослесаря");
        assertTrue(mechanicIds.contains(101), "ID автослесаря должен быть 101");
        assertTrue(mechanicIds.contains(102), "ID автослесаря должен быть 102");
    }

    @Test
    public void testGetMechanicsForRequest() {
        RequestMechanicsDAO requestMechanicsDAO = new RequestMechanicsDAO();
        // Добавляем тестовые данные
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Request_Mechanics (request_id, employee_id) VALUES (3, 101)");
            stmt.execute("INSERT INTO Request_Mechanics (request_id, employee_id) VALUES (3, 103)");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении тестовых данных", e);
        }

        List<Integer> mechanicIds = requestMechanicsDAO.getMechanicsForRequest(3);
        assertNotNull(mechanicIds, "Список идентификаторов не должен быть null");
        assertEquals(2, mechanicIds.size(), "Должно быть 2 автослесаря");
        assertTrue(mechanicIds.contains(101), "ID автослесаря должен быть 101");
        assertTrue(mechanicIds.contains(103), "ID автослесаря должен быть 103");
    }

    @Test
    public void testGetMechanicsForRequestNotFound() {
        RequestMechanicsDAO requestMechanicsDAO = new RequestMechanicsDAO();
        List<Integer> mechanicIds = requestMechanicsDAO.getMechanicsForRequest(999); // Не существующий request_id

        assertNotNull(mechanicIds, "Список идентификаторов не должен быть null");
        assertTrue(mechanicIds.isEmpty(), "Список должен быть пустым для несуществующего request_id");
    }
}