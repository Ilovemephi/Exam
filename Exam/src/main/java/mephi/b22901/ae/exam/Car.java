/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam;



public class Car {
    private int id; 
    private String brand; 
    private String model; 
    
 
    public Car(String brand, String model) {
        this.id = 0; 
        this.brand = brand;
        this.model = model;
    }
    
    
    public Car(int id, String brand, String model) {
        this.id = id;
        this.brand = brand;
        this.model = model;
    }
    
  
    public int getId() {
        return id;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    

    public void setId(int id) {
        this.id = id;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    @Override
    public String toString() {
        return "Car{id=" + id + ", brand='" + brand + "', model='" + model + "'}";
    }
}
