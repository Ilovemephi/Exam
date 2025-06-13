/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import mephi.b22901.ae.exam.DAO.PartDAO;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.Service;

/**
 * Класс для генерации запчастей для поломок автомобиля.
 * Для каждой услуги выбирает 1 или 2 случайные запчасти из таблицы Parts,
 * соответствующие категории и подкатегории услуги.
 */
public class CarPartsGenerator {


    

    public List<Part> generatePartsForServices(List<Service> services) {
        List<Part> breakdownParts = new ArrayList<>();
        PartDAO partDAO = new PartDAO();
        List<Part> allParts = partDAO.getAllParts();
        Set<String> partCategories = new HashSet<>();
        for (Part part : allParts) {
            if ("Сервисное обслуживание".equals(part.getCategory()))
                continue;
            partCategories.add(part.getCategory());
        }
        List<String> categoriesList = new ArrayList<>(partCategories);

        List<String> selectedCategories = addCategories(0.9, categoriesList);
        
        Set<String> subcategories = new HashSet<>();
        
        for (Part part : allParts){
            if (selectedCategories.contains(part.getCategory()))
                subcategories.add(part.getSubcategory());
        }
        
        List<String> subcategoriesList = new ArrayList<>(subcategories);
        List<String> selectedSubcategories = addCategories(0.85, subcategoriesList);
        

        for (Part part : allParts){
            if (selectedSubcategories.contains(part.getSubcategory()))
                breakdownParts.add(part);
        }

        return breakdownParts;
    }
    
    private List<String> addCategories(double firstPercent, List<String> categoriesList){
        List <String> selectedCategories = new ArrayList<>();
         Random random = new Random();

        if (random.nextDouble() < firstPercent) {
            // firstPercent случаев 
            Collections.shuffle(categoriesList);
            selectedCategories.add(categoriesList.get(0));
        } else {
            // 100 - firstPercent случаев — 2 разные категории
            Collections.shuffle(categoriesList);
            selectedCategories.add(categoriesList.get(0));
            selectedCategories.add(categoriesList.get(1));
        }
        
        return selectedCategories;
        
    }
    
    
}
