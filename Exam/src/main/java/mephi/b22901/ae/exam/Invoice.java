
package mephi.b22901.ae.exam;

/**
 * Класс, представляющий счёт за услуги автосервиса. Содержит информацию о счёте,
 * включая идентификатор, идентификатор связанной заявки, идентификатор клиента,
 * идентификатор мастера и общую сумму. Предназначен для хранения и управления данными
 * счёта в системе автосервиса. Идентификатор устанавливается автоматически при добавлении
 * счёта в базу данных (по умолчанию 0 для новых объектов) и может быть обновлён через
 * метод {@code setId}.
 *
 * @author artyom_egorkin
 */

public class Invoice {
    private int id; 
    private int requestId; 
    private int clientId; 
    private int masterId; 
    private int totalAmount; 
    
    
    public Invoice(int requestId, int clientId, int masterId, int totalAmount) {
        this.id = 0; 
        this.requestId = requestId;
        this.clientId = clientId;
        this.masterId = masterId;
        this.totalAmount = totalAmount;
    }
    

    public Invoice(int id, int requestId, int clientId, int masterId, int totalAmount) {
        this.id = id;
        this.requestId = requestId;
        this.clientId = clientId;
        this.masterId = masterId;
        this.totalAmount = totalAmount;
    }
    
    
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