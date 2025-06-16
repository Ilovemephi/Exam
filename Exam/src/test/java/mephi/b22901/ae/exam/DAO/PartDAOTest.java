/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import org.junit.jupiter.api.*;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.DAO.PartDAO;
import mephi.b22901.ae.exam.Connection.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartDAOTest {

    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    @BeforeAll
    public static void setUpBeforeAll() {
        // Переключаемся на H2 для тестов
        DBConnection.useUrl(H2_URL, H2_USER, H2_PASSWORD);
        // Инициализация таблицы Parts
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Parts");
            stmt.execute("CREATE TABLE Parts (" +
                    "part_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "category VARCHAR(50), " +
                    "subcategory VARCHAR(50), " +
                    "price DOUBLE)");
            System.out.println("Таблица Parts создана успешно");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при настройке тестовой БД", e);
        }
    }

    @AfterAll
    public static void tearDownAfterAll() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Parts");
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
        // Очистка таблицы перед каждым тестом и добавление тестовых данных
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Parts");
            stmt.execute("INSERT INTO Parts (name, category, subcategory, price) VALUES ('Ремень ГРМ', 'ДВС', 'ГРМ', 1500.50)");
            stmt.execute("INSERT INTO Parts (name, category, subcategory, price) VALUES ('Амортизатор', 'Ходовая', 'Передняя подвеска', 3000.75)");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке или заполнении таблицы", e);
        }
    }

    

    @Test
    public void testGetPartById() {
        PartDAO partDAO = new PartDAO();
        Part part = partDAO.getPartById(1);

        assertNotNull(part, "Запчасть должна быть найдена");
        assertEquals(1, part.getId(), "ID должен быть 1");
        assertEquals("Ремень ГРМ", part.getName(), "Название должно совпадать");
        assertEquals("ДВС", part.getCategory(), "Категория должна совпадать");
        assertEquals("ГРМ", part.getSubcategory(), "Подкатегория должна совпадать");
        assertEquals(1500.50, part.getPrice(), "Цена должна совпадать");
    }

    @Test
    public void testGetPartByIdNotFound() {
        PartDAO partDAO = new PartDAO();
        Part part = partDAO.getPartById(999); 

        assertNull(part, "Запчасть с ID 999 не должна быть найдена");
    }
}