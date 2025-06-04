
package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */

public class Invoice {
    private int id; // invoice_id (SERIAL, генерируется базой)
    private int requestId; // request_id (INTEGER, NOT NULL, FK на Requests)
    private int clientId; // client_id (INTEGER, NOT NULL, FK на Clients)
    private int masterId; // master_id (INTEGER, NOT NULL, FK на Employees)
    private int totalAmount; // total_amount (INTEGER, NOT NULL)
    
    // Конструктор для нового счёта (без id)
    public Invoice(int requestId, int clientId, int masterId, int totalAmount) {
        this.id = 0; // Явная инициализация для нового счёта
        this.requestId = requestId;
        this.clientId = clientId;
        this.masterId = masterId;
        this.totalAmount = totalAmount;
    }
    
    // Конструктор для загрузки из базы (с id)
    public Invoice(int id, int requestId, int clientId, int masterId, int totalAmount) {
        this.id = id;
        this.requestId = requestId;
        this.clientId = clientId;
        this.masterId = masterId;
        this.totalAmount = totalAmount;
    }
    
    // Геттеры
    public int getId() {
        return id;
    }
    
    public int getRequestId() {
        return requestId;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public int getMasterId() {
        return masterId;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    @Override
    public String toString() {
        return "Invoice{id=" + id + ", requestId=" + requestId + ", clientId=" + clientId + 
               ", masterId=" + masterId + ", totalAmount=" + totalAmount + "}";
    }
}