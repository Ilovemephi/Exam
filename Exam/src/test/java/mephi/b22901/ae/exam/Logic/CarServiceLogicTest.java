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
      
        logic = new CarServiceLogic();

        
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
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