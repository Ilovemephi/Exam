package mephi.b22901.ae.exam;

/**
 * Класс, представляющий сотрудника автосервиса. Содержит информацию о сотруднике,
 * включая идентификатор, полное имя и роль (например, "Мастер-приёмщик" или "Автослесарь").
 * Предназначен для хранения и управления данными сотрудников в системе автосервиса.
 * Идентификатор устанавливается автоматически при добавлении сотрудника в базу данных
 * (по умолчанию 0 для новых объектов) и может быть обновлён через метод {@code setId}.
 *
 * @author artyom_egorkin
 */

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
