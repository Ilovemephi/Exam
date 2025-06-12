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
    
    public Client(String fullName, String phoneNumber, String carNumber) {
        this.id = 0; 
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
    }
    
    public Client(int id, String fullName, String phoneNumber, String carNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
    }
    
    public int getId(){
        return id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getphoneNumber() {
        return phoneNumber;
    }
    
    public String getcarNumber() {
        return carNumber;
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
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    @Override
    public String toString() {
        return "Client{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", carNumber='" + carNumber + '\'' +
               '}';
    }
    
}
