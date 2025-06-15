/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Random;

import mephi.b22901.ae.exam.DAO.PartDAO;

import java.util.*;
import mephi.b22901.ae.exam.Part;


public class CarPartsGenerator {

    private final Random random = new Random();

    public List<Part> generatePartsForServices() {
        List<Part> breakdownParts = new ArrayList<>();
        PartDAO partDAO = new PartDAO();
        List<Part> allParts = partDAO.getAllParts();

        // 1. Выбираем все категории, кроме сервисного обслуживания
        Set<String> partCategories = new HashSet<>();
        for (Part part : allParts) {
            if (!"Сервисное обслуживание".equals(part.getCategory())) {
                partCategories.add(part.getCategory());
            }
        }

        List<String> categoriesList = new ArrayList<>(partCategories);
        List<String> selectedCategories = addCategories(90, categoriesList);

        // 2. Выбираем подкатегории из выбранных категорий
        Set<String> subcategories = new HashSet<>();
        for (Part part : allParts) {
            if (selectedCategories.contains(part.getCategory())) {
                subcategories.add(part.getSubcategory());
            }
        }

        List<String> subcategoriesList = new ArrayList<>(subcategories);
        List<String> selectedSubcategories = addCategories(85, subcategoriesList);

        // 3. Для каждой выбранной подкатегории выбираем случайные детали 
        for (String subcategory : selectedSubcategories) {
            List<Part> matchingParts = new ArrayList<>();
            for (Part part : allParts) {
                if (subcategory.equals(part.getSubcategory())) {
                    matchingParts.add(part);
                }
            }

            if (!matchingParts.isEmpty()) {
                Collections.shuffle(matchingParts);
                int count = (random.nextDouble() < 0.3 && matchingParts.size() > 1) ? 2 : 1;
                for (int i = 0; i < count && i < matchingParts.size(); i++) {
                    breakdownParts.add(matchingParts.get(i));
                }

            }
        }

        return breakdownParts;
    }

    private List<String> addCategories(double firstPercent, List<String> categoriesList) {
        List<String> selectedCategories = new ArrayList<>();
        Collections.shuffle(categoriesList);

        if (categoriesList.isEmpty()) return selectedCategories;

        if (random.nextDouble() < firstPercent / 100.0 || categoriesList.size() == 1) {
            selectedCategories.add(categoriesList.get(0));
        } else if (categoriesList.size() >= 2) {
            selectedCategories.add(categoriesList.get(0));
            selectedCategories.add(categoriesList.get(1));
        }

        return selectedCategories;
    }
}
