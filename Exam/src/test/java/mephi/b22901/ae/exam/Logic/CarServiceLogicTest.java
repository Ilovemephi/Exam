package mephi.b22901.ae.exam.Logic;

import java.io.IOException;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.Invoice;
import mephi.b22901.ae.exam.Request;
import mephi.b22901.ae.exam.Logic.CarServiceLogic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CarServiceLogicTest {

    private CarServiceLogic logic;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Переключаемся на H2 для тестов
        System.setProperty("env", "test");
        // Инициализация H2 базы данных в памяти
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        logic = new CarServiceLogic();

        // Создание таблиц и вставка тестовых данных
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS clients");
        statement.execute("CREATE TABLE clients (client_id IDENTITY PRIMARY KEY, full_name VARCHAR(100), phone_number VARCHAR(15) UNIQUE, car_number VARCHAR(10))");
        statement.execute("INSERT INTO clients (full_name, phone_number, car_number) VALUES ('Иван Иванов', '+79991234567', 'A123BC')");

        statement.execute("DROP TABLE IF EXISTS requests");
        statement.execute("CREATE TABLE requests (request_id IDENTITY PRIMARY KEY, client_id INT, status VARCHAR(50), description VARCHAR(255))");
        statement.execute("DROP TABLE IF EXISTS employees");
        statement.execute("CREATE TABLE employees (employee_id IDENTITY PRIMARY KEY, full_name VARCHAR(100), role VARCHAR(50))");
        statement.execute("INSERT INTO employees (full_name, role) VALUES ('Мастер Иванов', 'Мастер-приёмщик')");
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Очистка данных и закрытие соединения
        if (connection != null && !connection.isClosed()) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS clients");
            statement.execute("DROP TABLE IF EXISTS requests");
            statement.execute("DROP TABLE IF EXISTS employees");
            connection.close();
        }
    }

    @Test
    void testCreateNewRequest_Success() throws SQLException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        assertNotNull(request, "Заявка должна быть создана");
        assertEquals("Новая заявка", request.getStatus(), "Статус должен быть 'Новая заявка'");
    }

    @Test
    void testCreateNewRequest_NullClient() {
        try {
            logic.createNewRequest(null, "Поломка");
            fail("Должен быть выброшен IllegalArgumentException при null клиенте");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
        }
    }

    @Test
    void testConductDiagnostics2_Success() throws SQLException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request);
        logic.conductDiagnostics2(request);
        assertEquals("Проведена диагностика", request.getStatus(), "Статус должен измениться на 'Проведена диагностика'");
    }

    @Test
    void testConductDiagnostics2_InvalidStatus() throws SQLException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        request.setStatus("Проведена работа"); // Некорректный статус
        try {
            logic.conductDiagnostics2(request);
            fail("Должен быть выброшен IllegalStateException при неверном статусе");
        } catch (IllegalStateException e) {
            // Ожидаемое исключение
        }
    }

    @Test
    void testAssignMaster_Success() throws SQLException {
        Request request = new Request(1, "Поломка", "Новая заявка");
        logic.assignMaster(request);
        assertNotNull(request.getMasterId(), "Мастер должен быть назначен");
    }

    @Test
    void testViewInvoice_Success() throws SQLException, IOException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request);
        logic.conductDiagnostics2(request);
        logic.assignMechanic(request);
        logic.performWork(request);
        Invoice invoice = logic.viewInvoice(request);
        assertNotNull(invoice, "Счёт должен быть создан");
        assertTrue(invoice.getTotalAmount() > 0, "Сумма должна быть положительной");
    }

    @Test
    void testViewInvoice_DeclineRepair() throws SQLException, IOException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request);
        logic.conductDiagnostics2(request);
        logic.declineRepair(request);
        Invoice invoice = logic.viewInvoice(request);
        assertNotNull(invoice, "Счёт должен быть создан");
        assertTrue(invoice.getTotalAmount() > 0, "Сумма за диагностику должна быть положительной");
    }
}