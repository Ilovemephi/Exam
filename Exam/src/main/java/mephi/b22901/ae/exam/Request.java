
package mephi.b22901.ae.exam;


public class Request {
    private int id; 
    private int clientId; 
    private String reason; 
    private String status; 
    private String diagnosticResult; 
    private Integer masterId; 
    private Integer mechanicId; 
    private String workResult; 
    

    public Request(int clientId, String reason, String status) {
        this.id = 0; 
        this.clientId = clientId;
        this.reason = reason;
        this.status = status;
        this.diagnosticResult = null;
        this.masterId = null;
        this.mechanicId = null;
        this.workResult = null;
    }
    
   
    public Request(int id, int clientId, String reason, String status, String diagnosticResult, 
                   Integer masterId, Integer mechanicId, String workResult) {
        this.id = id;
        this.clientId = clientId;
        this.reason = reason;
        this.status = status;
        this.diagnosticResult = diagnosticResult;
        this.masterId = masterId;
        this.mechanicId = mechanicId;
        this.workResult = workResult;
    }
    

    public int getRequestId() {
        return id;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getDiagnosticResult() {
        return diagnosticResult;
    }
    
    public Integer getMasterId() {
        return masterId;
    }
    
    public Integer getMechanicId() {
        return mechanicId;
    }
    
    public String getWorkResult() {
        return workResult;
    }
    

    public void setId(int id) {
        this.id = id;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setDiagnosticResult(String diagnosticResult) {
        this.diagnosticResult = diagnosticResult;
    }
    
    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }
    
    public void setMechanicId(Integer mechanicId) {
        this.mechanicId = mechanicId;
    }
    
    public void setWorkResult(String workResult) {
        this.workResult = workResult;
    }
    
    @Override
    public String toString() {
        return "Request{id=" + id + ", clientId=" + clientId + ", reason='" + reason + "', status='" + status + 
               "', diagnosticResult='" + diagnosticResult + "', masterId=" + masterId + 
               ", mechanicId=" + mechanicId + ", workResult='" + workResult + "'}";
    }
}
