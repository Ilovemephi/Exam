/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mephi.b22901.ae.exam.Random;

import java.util.List;
import mephi.b22901.ae.exam.DAO.PartDAO;
import mephi.b22901.ae.exam.Part;
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
public class CarMaintenanceGeneratorTest {
    
   private CarMaintenanceGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new CarMaintenanceGenerator();
    }

    @Test
    void testGeneratePartsForMaintenance_ReturnsOnlyMaintenanceParts() {
        List<Part> maintenanceParts = generator.generatePartsForMaintenance();
        for (Part part : maintenanceParts) {
            assertEquals("Сервисное обслуживание", part.getCategory(), 
                    "Все запчасти должны быть из категории 'Сервисное обслуживание'");
        }
    }
    
    @Test
    void testGeneratePartsForMaintenance_RandomSize() {
        List<Part> firstRun = generator.generatePartsForMaintenance();
        List<Part> secondRun = generator.generatePartsForMaintenance();
        int maxSize = 0;
        PartDAO partDAO = new PartDAO();
        List<Part> allParts = partDAO.getAllParts();
        for (Part part : allParts) {
            if ("Сервисное обслуживание".equals(part.getCategory())) {
                maxSize++;
            }
        }

        assertTrue(firstRun.size() >= 0 && firstRun.size() <= maxSize,  "Размер списка должен быть от 0 до " + maxSize);
        assertTrue(secondRun.size() >= 0 && secondRun.size() <= maxSize,  "Размер списка должен быть от 0 до " + maxSize);
    }

    @Test
    void testGeneratePartsForMaintenance_NonEmptyWhenMaintenancePartsExist() {
        List<Part> maintenanceParts = generator.generatePartsForMaintenance();
        assertFalse(maintenanceParts.isEmpty(), 
                "Список не должен быть пустым, если есть запчасти для сервисного обслуживания");
    }
    
}
