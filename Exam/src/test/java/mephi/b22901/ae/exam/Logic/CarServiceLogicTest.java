/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.Logic;

import java.io.File;
import java.io.IOException;
import java.util.List;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.Invoice;
import mephi.b22901.ae.exam.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author artyom_egorkin
 */
public class CarServiceLogicTest {
    
    private CarServiceLogic logic;
    
    @BeforeEach
    void setUp() {
        logic = new CarServiceLogic(); 
    }
    
    
    @Test
    void testCreateNewRequest_Success() {
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
            
        }
    }
    
    @Test
    void testConductDiagnostics2_Success() {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request); 
        logic.conductDiagnostics2(request);
        assertEquals("Проведена диагностика", request.getStatus(), "Статус должен измениться на 'Проведена диагностика'");
    }
    
    @Test
    void testConductDiagnostics2_InvalidStatus() {
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
    void testAssignMaster_Success() {
        Request request = new Request(1, "Поломка", "Новая заявка");
        logic.assignMaster(request);
        assertNotNull(request.getMasterId(), "Мастер должен быть назначен");
    }
    
    

    @Test
    void testViewInvoice_Success() throws IOException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request);
        logic.conductDiagnostics2(request);
        logic.assignMechanic(request);
        logic.performWork(request); 
        mephi.b22901.ae.exam.Invoice invoice = logic.viewInvoice(request);
        assertNotNull(invoice, "Счёт должен быть создан");
        assertTrue(invoice.getTotalAmount() > 0, "Сумма должна быть положительной");
    }

    @Test
    void testViewInvoice_DeclineRepair() throws IOException {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        Request request = logic.createNewRequest(client, "Поломка");
        logic.assignMaster(request);
        logic.conductDiagnostics2(request);
        logic.declineRepair(request);
        mephi.b22901.ae.exam.Invoice invoice = logic.viewInvoice(request);
        assertNotNull(invoice, "Счёт должен быть создан");
        assertTrue(invoice.getTotalAmount() > 0, "Сумма за диагностику должна быть положительной");
    }

     
    
}
