/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import mephi.b22901.ae.exam.DAO.PartDAO;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.Service;

/**
 *
 * @author artyom_egorkin
 */

/**
 * Класс для случайных категорий сервисного обслуживания 
 * @author artyom_egorkin
 */
public class CarMaintenanceGenerator {
    private final Random random = new Random();
    
    /**
     * Метод генерирует категории Сервисного обслуживания 
     * @return Возвращает список категорий сервисного обслуживания 
     */
    public List<Part> generatePartsForMaintenance () {
        List<Part> maintenanceParts = new ArrayList<>();
        List<Part> maintenancePartsTemp = new ArrayList<>();
        PartDAO partDAO = new PartDAO();
        List<Part> allParts = partDAO.getAllParts();
        for (Part part : allParts) {
            if ("Сервисное обслуживание".equals(part.getCategory())) {
               maintenancePartsTemp.add(part);
            }
        }
        
        int rand = random.nextInt(maintenancePartsTemp.size());
        Collections.shuffle(maintenancePartsTemp);
        for (int i = 0; i <= rand; i++){
            maintenanceParts.add(maintenancePartsTemp.get(i));
        }
        
        return maintenanceParts;
    }
     
   
}
