/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Logic;

import java.util.List;
import java.util.Random;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.DAO.ClientDAO;
import mephi.b22901.ae.exam.DAO.EmployeeDAO;
import mephi.b22901.ae.exam.DAO.RequestDAO;
import mephi.b22901.ae.exam.Employee;
import mephi.b22901.ae.exam.Request;

/**
 *
 * @author artyom_egorkin
 */
public class CarServiceLogic {
    
    private final ClientDAO clientDAO = new ClientDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final Random random = new Random();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();


    
    public Request createNewRequest(Client client) {
        Client dbClient = clientDAO.getClientByPhone(client.getPhoneNumber());
        int clientId;
        if (dbClient == null) {
            clientId = clientDAO.addClient(client);
        } else {
            clientId = dbClient.getId();
        }

        boolean isService = random.nextBoolean();
        String reason;
        if (isService) {
            reason = "Сервисное обслуживание";
        } else {
            reason = "Поломка";
        }

        String status = "Новая заявка";

        Request request = new Request(clientId, reason, status);
        int requestId = requestDAO.addRequest(request);
        request.setId(requestId);

        return request;
    }
    
    
    public void assignMaster(Request request) {
        // 1. Получить список всех мастеров-приёмщиков
        List<Employee> masters = employeeDAO.getEmployeesByRole("Мастер-приёмщик");
        if (masters == null || masters.isEmpty()) {
            throw new RuntimeException("Нет ни одного мастера-приёмщика!");
        }

        // 2. Случайно выбрать одного из них
        Employee selectedMaster = masters.get(random.nextInt(masters.size()));

        // 3. Установить его ID в заявку
        request.setMasterId(selectedMaster.getId());

        // 4. Обновить заявку в базе данных
        requestDAO.updateRequest(request);

        // 5. (Необязательно) Сообщить о выборе мастера
        System.out.println("Назначен мастер-приёмщик: " + selectedMaster.getFullName());
    }


    
    
    
}
