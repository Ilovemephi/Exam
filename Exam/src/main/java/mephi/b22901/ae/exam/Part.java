
package mephi.b22901.ae.exam;

/**
 * Класс, представляющий запчасть автосервиса. Содержит информацию о запчасти,
 * включая идентификатор, название, категорию, подкатегорию и цену.
 * Предназначен для хранения и управления данными запчастей в системе автосервиса.
 * Идентификатор устанавливается автоматически при добавлении запчасти в базу данных
 * (по умолчанию 0 для новых объектов) и может быть обновлён через метод {@code setId}.
 *
 * @author artyom_egorkin
 */

public class Part {
    private int id; 
    private String name;
    private String category; 
    private String subcategory; 
    private double price;

    public Part(String name, String category, String subcategory, double price) {
        this.id = 0; 
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
    }
    
   
    public Part(int id, String name, String category, String subcategory, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
    }
    

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getSubcategory() {
        return subcategory;
    }
    
    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    
}
