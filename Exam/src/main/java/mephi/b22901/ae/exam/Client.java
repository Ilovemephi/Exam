package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */
public class Client {
    
    private int id;
    private String fullName;
    private String phoneNumber;
    private String carNumber;
    private int carId;
    
    public Client(int id, String fullName, String phoneNumber, String carNumber, int carId) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
    }
    
    public Client(String fullName, String phoneNumber, String carNumber, int carId) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
    
    public int getId(){
        return id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String phoneNumber() {
        return phoneNumber;
    }
    
    public String carNumber() {
        return carNumber;
    }
    
    public int carId() {
        return carId;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    @Override
    public String toString() {
        return "Client{id=" + id + ", fullName='" + fullName + "', phoneNumber='" + phoneNumber + "', carNumber='" + carNumber + "', carId=" + carId + "}";
    }
    
}
