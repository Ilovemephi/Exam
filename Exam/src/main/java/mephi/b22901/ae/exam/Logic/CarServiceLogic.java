/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Logic;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.DAO.ClientDAO;
import mephi.b22901.ae.exam.DAO.EmployeeDAO;
import mephi.b22901.ae.exam.DAO.RequestDAO;
import mephi.b22901.ae.exam.DAO.RequestPartDAO;
import mephi.b22901.ae.exam.Employee;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.Random.CarMaintenanceGenerator;
import mephi.b22901.ae.exam.Random.CarPartsGenerator;
import mephi.b22901.ae.exam.Request;
import mephi.b22901.ae.exam.RequestPart;

/**
 *
 * @author artyom_egorkin
 */
public class CarServiceLogic {
    
    private final ClientDAO clientDAO = new ClientDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final Random random = new Random();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();


    
//    public Request createNewRequest(Client client) {
//        Client dbClient = clientDAO.getClientByPhone(client.getPhoneNumber());
//        int clientId;
//        if (dbClient == null) {
//            clientId = clientDAO.addClient(client);
//        } else {
//            clientId = dbClient.getId();
//        }
//
//        boolean isService = random.nextBoolean();
//        String reason;
//        if (isService) {
//            reason = "Сервисное обслуживание";
//        } else {
//            reason = "Поломка";
//        }
//
//        String status = "Новая заявка";
//
//        Request request = new Request(clientId, reason, status);
//        int requestId = requestDAO.addRequest(request);
//        request.setId(requestId);
//
//        return request;
//    }
    
    
    public Request createNewRequest(Client client, String reason) {
        if (client == null || reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Клиент и причина обращения не могут быть пустыми");
        }

        int clientId;

        Client existingClient = clientDAO.getClientByPhone(client.getPhoneNumber());
        if (existingClient == null) {

            clientId = clientDAO.addClient(client);
        } else {

            clientId = existingClient.getId();
        }

        Request request = new Request(clientId, reason, "Новая заявка");
        int requestId = requestDAO.addRequest(request);
        Request createdRequest = requestDAO.getRequestById(requestId);
        if (createdRequest == null) {
            throw new RuntimeException("Не удалось получить созданную заявку");
        }
        return createdRequest;
    }
    
    
    public void assignMaster(Request request) { // Назначем именно ПРИЁМЩИКА
        List<Employee> masters = employeeDAO.getEmployeesByRole("Мастер-приёмщик");
        if (masters == null || masters.isEmpty()) {
            throw new RuntimeException("Нет ни одного мастера-приёмщика!");
        }
        
        Employee selectedMaster = masters.get(random.nextInt(masters.size()));
        request.setMasterId(selectedMaster.getId());
        requestDAO.updateRequest(request);
        System.out.println("Назначен мастер-приёмщик: " + selectedMaster.getFullName());
    }
    


    public void conductDiagnostics(Request request) {
        if (request.getMasterId() == null) {
        throw new IllegalStateException("Мастер-приёмщик не назначен, диагностика невозможна.");
        }
        if (!"новая заявка".equalsIgnoreCase(request.getStatus())) {
        throw new IllegalStateException("Диагностику можно проводить только для новых заявок.");
        }
        if ("сервисное обслуживание".equalsIgnoreCase(request.getReason())) {
            throw new IllegalStateException("Диагностика не требуется для сервисного обслуживания.");
        }
        
        CarPartsGenerator generator = new CarPartsGenerator();
        List<Part> diagnosedParts = generator.generatePartsForServices();
        RequestPartDAO requestPartDAO = new RequestPartDAO();
        for (Part part : diagnosedParts) {
            requestPartDAO.addRequestPart(new RequestPart(request.getRequestId(), part.getId()));
        }
        
        if (diagnosedParts.isEmpty()) {
            System.out.println("Диагностика: неисправности не обнаружены.");
            request.setDiagnosticResult("Неисправности не обнаружены");
        } else {
            System.out.print("Диагностика выявила: ");
            for (int i = 0; i < diagnosedParts.size(); i++) {
                System.out.print(diagnosedParts.get(i).getName());
                if (i != diagnosedParts.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
        request.setDiagnosticResult("Обнаружены неисправности");
        // Меняем статус заявки
        request.setStatus("Диагностика");
        requestDAO.updateRequest(request);
    }
    
    public void conductMaintenance(Request request) {
        if (request.getMasterId() == null) {
            throw new IllegalStateException("Мастер-приёмщик не назначен, обслуживание невозможно.");
        }
        if (!"новая заявка".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Обслуживание можно проводить только для новых заявок.");
        }
        if (!"сервисное обслуживание".equalsIgnoreCase(request.getReason())) {
            throw new IllegalStateException("Метод conductMaintenance предназначен только для сервисного обслуживания.");
        }

        CarMaintenanceGenerator generator = new CarMaintenanceGenerator();
        List<Part> maintenanceParts = generator.generatePartsForMaintenance();
        RequestPartDAO requestPartDAO = new RequestPartDAO();
        for (Part part : maintenanceParts) {
            requestPartDAO.addRequestPart(new RequestPart(request.getRequestId(), part.getId()));
        }

        if (maintenanceParts.isEmpty()) {
            System.out.println("Обслуживание: обслуживание не требуется.");
        } else {
            System.out.print("Обслуживание запланировано: ");
            for (int i = 0; i < maintenanceParts.size(); i++) {
                System.out.print(maintenanceParts.get(i).getName());
                if (i != maintenanceParts.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
        // Меняем статус заявки
        request.setStatus("Сервисное обслуживание");
        requestDAO.updateRequest(request);
    }
    
    
    
    
}
