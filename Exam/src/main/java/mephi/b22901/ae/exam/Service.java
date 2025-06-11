
package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */

public class Service {
    private int id; 
    private String category; 
    private String subcategory; 
    private double price; 
    private String requiredMechanicRole; 
    

    public Service(String category, String subcategory, double price, String requiredMechanicRole) {
        this.id = 0; 
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.requiredMechanicRole = requiredMechanicRole;
    }
    
   
    public Service(int id, String category, String subcategory, double price, String requiredMechanicRole) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.requiredMechanicRole = requiredMechanicRole;
    }
    

    public int getId() {
        return id;
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
    
    public String getRequiredMechanicRole() {
        return requiredMechanicRole;
    }
    

    public void setId(int id) {
        this.id = id;
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
    
    public void setRequiredMechanicRole(String requiredMechanicRole) {
        this.requiredMechanicRole = requiredMechanicRole;
    }
    
    @Override
    public String toString() {
        return "Service{id=" + id + ", category='" + category + "', subcategory='" + subcategory + 
               "', price=" + price + ", requiredMechanicRole='" + requiredMechanicRole + "'}";
    }
}
