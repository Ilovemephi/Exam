/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam;

/**
 * Класс, представляющий связь между заявкой и запчастью в автосервисе.
 * Содержит идентификатор заявки и идентификатор запчасти, связанной с этой заявкой.
 * Предназначен для хранения и управления данными о запчастях, используемых в заявках,
 * в системе автосервиса.
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
