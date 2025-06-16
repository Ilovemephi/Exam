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
public class CarPartsGeneratorTest {
    
    private CarPartsGenerator generator;
    private PartDAO partDAO;

    @BeforeEach
    void setUp() {
        generator = new CarPartsGenerator();
        partDAO = new PartDAO();
    }
    
    
    @Test
    void testGeneratePartsForServices_ReturnsOnlyNonMaintenanceParts() {
        List<Part> breakdownParts = generator.generatePartsForServices();
        for (Part part : breakdownParts) {
            assertNotEquals("Сервисное обслуживание", part.getCategory(), 
                    "Запчасти не должны быть из категории 'Сервисное обслуживание'");
        }
    }
    
    
    @Test
    void testGeneratePartsForServices_RandomSizeAndSubcategories() {
        List<Part> firstRun = generator.generatePartsForServices();
        List<Part> secondRun = generator.generatePartsForServices();

        int maxParts = partDAO.getAllParts().size(); 
        assertTrue(firstRun.size() >= 0 && firstRun.size() <= maxParts, 
                "Размер списка должен быть от 0 до " + maxParts);
        assertTrue(secondRun.size() >= 0 && secondRun.size() <= maxParts, 
                "Размер списка должен быть от 0 до " + maxParts);

        if (!firstRun.isEmpty() && !secondRun.isEmpty()) {
            String firstSubcategory = firstRun.get(0).getSubcategory();
            String secondSubcategory = secondRun.get(0).getSubcategory();
            assertNotNull(firstSubcategory, "Подкатегория должна быть определена");
            assertNotNull(secondSubcategory, "Подкатегория должна быть определена");
        }
    }
   
}
