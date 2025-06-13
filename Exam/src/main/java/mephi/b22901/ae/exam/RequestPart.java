/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam;

/**
 *
 * @author artyom_egorkin
 */
public class RequestPart {
    private int requestId;
    private int partId;

    public RequestPart(int requestId, int partId) {
        this.requestId = requestId;
        this.partId = partId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Override
    public String toString() {
        return "RequestPart{" +
                "requestId=" + requestId +
                ", partId=" + partId +
                '}';
    }

}
