/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.Service;

/**
 * Класс для генерации запчастей для поломок автомобиля.
 * Для каждой услуги выбирает 1 или 2 случайные запчасти из таблицы Parts,
 * соответствующие категории и подкатегории услуги.
 */
public class CarPartsGenerator {
    private final List<Part> parts; 
    private final Random random; 

    /**
     * Конструктор.
     * @param parts Список всех запчастей из таблицы Parts.
     */
    public CarPartsGenerator(List<Part> parts) {
        this.parts = parts;
        this.random = new Random();
    }

    public List<Part> generatePartsForServices(List<Service> services) {
        List<Part> selectedParts = new ArrayList<>();

        for (Service service : services) {
            String category = service.getCategory();
            if ("Диагностика".equals(category) || "Сервисное обслуживание".equals(category)) {
                continue;
            }
            if (random.nextBoolean()) {
                List<Part> matchingParts = new ArrayList<>();
                for (Part part : parts) {
                    if (part.getCategory().equals(category)
                            && part.getSubcategory().equals(service.getSubcategory())) {
                        matchingParts.add(part);
                    }
                }
                if (!matchingParts.isEmpty()) {
                    int numParts = random.nextBoolean() ? 1 : 2;
                    numParts = Math.min(numParts, matchingParts.size());
                    Collections.shuffle(matchingParts, random);
                    for (int i = 0; i < numParts; i++) {
                        selectedParts.add(matchingParts.get(i));
                    }
                }
            }
        }

        return selectedParts;
    }
}
