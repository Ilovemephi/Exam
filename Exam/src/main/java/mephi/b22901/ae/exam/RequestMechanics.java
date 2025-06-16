/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam;

/**
 * Класс, представляющий связь между заявкой и сотрудником (механиком) в автосервисе.
 * Содержит идентификатор заявки и идентификатор сотрудника (сотрудников), назначенного для работы над заявкой.
 * Предназначен для хранения и управления данными о назначении механиков к заявкам в системе автосервиса.
 *
 * @author artyom_egorkin
 */
public class RequestMechanics {
    private int requestId;
    private int employeeId;

    public RequestMechanics(int requestId, int employeeId) {
        this.requestId = requestId;
        this.employeeId = employeeId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
