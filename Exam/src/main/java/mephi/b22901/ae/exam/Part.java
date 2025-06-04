
package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */

public class Part {
    private int id; 
    private String name;
    private String category; 
    private String subcategory; 
    

    public Part(String name, String category, String subcategory) {
        this.id = 0; 
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
    }
    
   
    public Part(int id, String name, String category, String subcategory) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
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
    
    @Override
    public String toString() {
        return "Part{id=" + id + ", name='" + name + "', category='" + category + "', subcategory='" + subcategory + "'}";
    }
}
