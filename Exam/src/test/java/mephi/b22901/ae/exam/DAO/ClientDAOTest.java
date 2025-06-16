/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.DAO;

import java.util.List;
import mephi.b22901.ae.exam.Client;
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
public class ClientDAOTest {
    
    private ClientDAO clientDAO;

    @BeforeEach
    void setUp() {
        clientDAO = new ClientDAO(); 
    }

    @Test
    void testAddClient_Success() {
        Client client = new Client("Иван Иванов", "+79991234567", "A123BC");
        int clientId = clientDAO.addClient(client);
        assertTrue(clientId > 0, "ID нового клиента должен быть больше 0");
        Client retrievedClient = clientDAO.getClientById(clientId);
        assertNotNull(retrievedClient, "Клиент должен быть найден");
        assertEquals("Иван Иванов", retrievedClient.getFullName(), "Имя клиента не совпадает");
    }

    
    
}
