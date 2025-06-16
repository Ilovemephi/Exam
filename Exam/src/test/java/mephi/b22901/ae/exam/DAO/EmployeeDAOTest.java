/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import org.junit.jupiter.api.*;
import mephi.b22901.ae.exam.Employee;
import mephi.b22901.ae.exam.DAO.EmployeeDAO;
import mephi.b22901.ae.exam.Connection.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import mephi.b22901.ae.exam.Connection.DBConnection;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    @BeforeAll
    public static void setUpBeforeAll() {
        // Переключаемся на H2 для тестов
        DBConnection.useUrl(H2_URL, H2_USER, H2_PASSWORD);
        // Инициализация таблицы Employees
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Employees");
            stmt.execute("CREATE TABLE Employees (" +
                    "employee_id SERIAL PRIMARY KEY, " +
                    "full_name VARCHAR(100), " +
                    "role VARCHAR(50))");
            System.out.println("Таблица Employees создана успешно");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при настройке тестовой БД", e);
        }
    }

    @AfterAll
    public static void tearDownAfterAll() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Employees");
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
            stmt.execute("DELETE FROM Employees");
            // Добавляем тестовые данные
            stmt.execute("INSERT INTO Employees (full_name, role) VALUES ('Иван Иванов', 'Мастер')");
            stmt.execute("INSERT INTO Employees (full_name, role) VALUES ('Петр Петров', 'Автослесарь')");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке или заполнении таблицы", e);
        }
    }

    @Test
    public void testGetEmployeeById() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeById(1);

        assertNotNull(employee, "Сотрудник должен быть найден");
        assertEquals(1, employee.getId(), "ID должен быть 1");
        assertEquals("Иван Иванов", employee.getFullName(), "ФИО должно совпадать");
        assertEquals("Мастер", employee.getRole(), "Роль должна совпадать");
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeById(999); // Не существующий ID

        assertNull(employee, "Сотрудник с ID 999 не должен быть найден");
    }

    @Test
    public void testGetAllEmployees() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.getAllEmployees();

        assertNotNull(employees, "Список сотрудников не должен быть null");
        assertEquals(2, employees.size(), "Должно быть 2 сотрудника");
        assertTrue(employees.stream().anyMatch(e -> e.getFullName().equals("Иван Иванов")), "Должен быть Иван Иванов");
        assertTrue(employees.stream().anyMatch(e -> e.getFullName().equals("Петр Петров")), "Должен быть Петр Петров");
    }

    @Test
    public void testGetEmployeesByRole() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.getEmployeesByRole("Мастер");

        assertNotNull(employees, "Список сотрудников не должен быть null");
        assertEquals(1, employees.size(), "Должен быть 1 сотрудник с ролью Мастер");
        assertEquals("Иван Иванов", employees.get(0).getFullName(), "ФИО должно совпадать");
        assertEquals("Мастер", employees.get(0).getRole(), "Роль должна совпадать");
    }

    @Test
    public void testGetEmployeesByRoleNotFound() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.getEmployeesByRole("Менеджер");

        assertNotNull(employees, "Список сотрудников не должен быть null");
        assertEquals(0, employees.size(), "Не должно быть сотрудников с ролью Менеджер");
    }
}