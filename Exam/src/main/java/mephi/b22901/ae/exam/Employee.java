package mephi.b22901.ae.exam;



public class Employee {
    private int id; 
    private String fullName; 
    private String role; 
    
  
    public Employee(String fullName, String role) {
        this.id = 0; 
        this.fullName = fullName;
        this.role = role;
    }
    
  
    public Employee(int id, String fullName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }
    
 
    public int getId() {
        return id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getRole() {
        return role;
    }
    

    public void setId(int id) {
        this.id = id;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "Employee{id=" + id + ", fullName='" + fullName + "', role='" + role + "'}";
    }
}
