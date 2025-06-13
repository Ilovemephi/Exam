/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */
public class RequestService {
    private int requestId;
    private int serviceId;

    public RequestService(int requestId, int serviceId) {
        this.requestId = requestId;
        this.serviceId = serviceId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "RequestService{" +
                "requestId=" + requestId +
                ", serviceId=" + serviceId +
                '}';
    }

}
