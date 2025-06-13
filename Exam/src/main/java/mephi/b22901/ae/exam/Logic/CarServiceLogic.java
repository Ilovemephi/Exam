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
import mephi.b22901.ae.exam.DAO.RequestPartDAO;
import mephi.b22901.ae.exam.Employee;
import mephi.b22901.ae.exam.Part;
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


    public void conductDiagnostic(Request request) {
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
        } else {
            System.out.print("Диагностика выявила: ");
            for (int i = 0; i < diagnosedParts.size(); i++) {
                System.out.print(diagnosedParts.get(i).getName());
                if (i != diagnosedParts.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
        // Меняем статус заявки
        request.setStatus("Проведена диагностика");
        requestDAO.updateRequest(request);
    }
    
    
}
