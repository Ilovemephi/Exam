/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.DAO.ClientDAO;
import mephi.b22901.ae.exam.DAO.EmployeeDAO;
import mephi.b22901.ae.exam.DAO.InvoiceDAO;
import mephi.b22901.ae.exam.DAO.PartDAO;
import mephi.b22901.ae.exam.DAO.RequestDAO;
import mephi.b22901.ae.exam.DAO.RequestMechanicsDAO;
import mephi.b22901.ae.exam.DAO.RequestPartDAO;
import mephi.b22901.ae.exam.DAO.RequestServiceDAO;
import mephi.b22901.ae.exam.DAO.ServiceDAO;
import mephi.b22901.ae.exam.Employee;
import mephi.b22901.ae.exam.Invoice;
import mephi.b22901.ae.exam.Part;
import mephi.b22901.ae.exam.Random.CarMaintenanceGenerator;
import mephi.b22901.ae.exam.Random.CarPartsGenerator;
import mephi.b22901.ae.exam.Request;
import mephi.b22901.ae.exam.RequestPart;
import mephi.b22901.ae.exam.RequestService;
import mephi.b22901.ae.exam.Service;

/**
 *
 * @author artyom_egorkin
 */
public class CarServiceLogic {
    
    private final ClientDAO clientDAO = new ClientDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final Random random = new Random();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final RequestPartDAO requestPartDAO = new RequestPartDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final RequestServiceDAO requestServiceDAO = new RequestServiceDAO();
    private final RequestMechanicsDAO requestMechanicsDAO = new RequestMechanicsDAO();


    
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
        request.setWorkResult("Работа не выполнена");
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
    
    public void conductDiagnostics2(Request request) {
        if (request == null || request.getRequestId() <= 0) throw new IllegalArgumentException("Некорректная заявка");
        if (request.getMasterId() == null) throw new IllegalStateException("Мастер-приемщик не назначен");
        if (!"Новая заявка".equalsIgnoreCase(request.getStatus())) throw new IllegalStateException("Диагностика проводится только для новых заявок");
        if ("сервисное обслуживание".equalsIgnoreCase(request.getReason())) throw new IllegalStateException("Диагностика не требуется для сервисного обслуживания");

        // 1. Сгенерировать найденные поломки (детали)
        CarPartsGenerator generator = new CarPartsGenerator();
        List<Part> breakdownParts = generator.generatePartsForServices();

        // 2. Сохранить детали к заявке
        for (Part part : breakdownParts) {
            requestPartDAO.addRequestPart(new RequestPart(request.getRequestId(), part.getId()));
        }

        // 3. Если поломок нет, сохранить результат и статус
        if (breakdownParts.isEmpty()) {
            request.setDiagnosticResult("Неисправностей не обнаружено.");
            request.setStatus("Диагностика");
            if (!requestDAO.updateRequest(request)) {
                throw new RuntimeException("Не удалось обновить заявку #" + request.getRequestId());
            }
            return;
        }

        // 4. Привязка услуг к заявке
        List<Service> allServices = serviceDAO.getAllServices();
        Set<String> usedSubcategories = new HashSet<>();
        for (Part part : breakdownParts) 
            usedSubcategories.add(part.getSubcategory());
        
        Map<String, Service> subcategoryToService = new HashMap<>();
        for (String subcat : usedSubcategories) {
            for (Service serv : allServices) {
                if (serv.getSubcategory().equalsIgnoreCase(subcat)) {
                    subcategoryToService.put(subcat, serv);
                    requestServiceDAO.addRequestService(new RequestService(request.getRequestId(), serv.getId()));
                    break;
                }
            }
        }

        // 5. Назначение механиков по ролям (уникальные роли)
        Set<String> assignedRoles = new HashSet<>();
        for (Service service : subcategoryToService.values()) {
            String mechanicRole = service.getRequiredMechanicRole();
            if (!assignedRoles.contains(mechanicRole)) {
                List<Employee> mechanics = employeeDAO.getEmployeesByRole(mechanicRole);
                if (!mechanics.isEmpty()) {
                    Employee mechanic = mechanics.get(random.nextInt(mechanics.size()));
                    requestMechanicsDAO.addMechanicToRequest(request.getRequestId(), mechanic.getId());
                }
                assignedRoles.add(mechanicRole);
            }
        }

        // 6. Сохраняем результат и статус
        request.setDiagnosticResult("Обнаружены неисправности"); 
        request.setStatus("Диагностика");
        if (!requestDAO.updateRequest(request)) {
            throw new RuntimeException("Не удалось обновить заявку #" + request.getRequestId());
        }
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
    

    
    public void conductRepair(Request request) {
        if (request == null || request.getRequestId() <= 0) {
            throw new IllegalArgumentException("Заявка недействительна");
        }
        if (!"Диагностика".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Ремонт можно проводить только после диагностики.");
        }

        // Проверка наличия назначенных автослесарей
        RequestMechanicsDAO mechanicsDAO = new RequestMechanicsDAO();
        List<Integer> mechanicIds = mechanicsDAO.getMechanicsForRequest(request.getRequestId());
        if (mechanicIds == null || mechanicIds.isEmpty()) {
            throw new IllegalStateException("Для ремонта должен быть назначен хотя бы один автослесарь.");
        }

        request.setStatus("Ремонт");
        boolean updated = requestDAO.updateRequest(request);
        if (!updated) {
            throw new RuntimeException("Не удалось обновить статус заявки на ремонт с ID: " + request.getRequestId());
        }
        System.out.println("Ремонт начат для заявки с ID: " + request.getRequestId());
    }
    
    
    public void completeRequest(Request request, java.io.File file) throws IOException {
        if (request == null || request.getRequestId() <= 0) {
            throw new IllegalArgumentException("Заявка недействительна");
        }
        if (!"Ремонт".equalsIgnoreCase(request.getStatus()) && !("Диагностика".equalsIgnoreCase(request.getStatus()) && "сервисное обслуживание".equalsIgnoreCase(request.getReason()))) {
            throw new IllegalStateException("Завершение возможно только для заявок в статусе 'Ремонт' или 'Диагностика' с причиной 'сервисное обслуживание'.");
        }

        // 1. Расчёт общей стоимости
        double totalCost = 0.0;
        List<RequestPart> requestParts = requestPartDAO.getRequestPartsByRequestId(request.getRequestId());
        List<RequestService> requestServices = requestServiceDAO.getRequestServicesByRequestId(request.getRequestId());
        for (RequestPart rp : requestParts) {
            Part part = new PartDAO().getPartById(rp.getPartId());
            if (part != null) {
                totalCost += part.getPrice();
            }
        }
        for (RequestService rs : requestServices) {
            Service service = new ServiceDAO().getServiceById(rs.getServiceId());
            if (service != null) {
                totalCost += service.getPrice();
            }
        }
        // 2. Проверка наличия автослесарей для ремонта
        if ("Ремонт".equalsIgnoreCase(request.getStatus())) {
            RequestMechanicsDAO mechanicsDAO = new RequestMechanicsDAO();
            List<Integer> mechanicIds = mechanicsDAO.getMechanicsForRequest(request.getRequestId());
            if (mechanicIds == null || mechanicIds.isEmpty()) {
                throw new IllegalStateException("Для завершения ремонта должен быть назначен хотя бы один автослесарь.");
            }
        }
        // 3. Создание счёта
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        Invoice invoice = new Invoice(request.getRequestId(), request.getClientId(), request.getMasterId(), (int) totalCost);
        int invoiceId = invoiceDAO.addInvoice(invoice);
        // 4. Сохранение счёта в файл
        saveInvoiceToTxt(invoice, file);
        System.out.println("Счёт сохранён в файл: " + file.getAbsolutePath());
        // 5. Обновление статуса заявки
        request.setStatus("Выполнена");
        request.setWorkResult("Работа выполнена");
        if (!requestDAO.updateRequest(request)) {
            throw new RuntimeException("Не удалось обновить статус заявки #" + request.getRequestId());
        }
        System.out.println("Заявка #" + request.getRequestId() + " завершена. Общая стоимость: " + totalCost);
    }
    

    private void saveInvoiceToTxt(Invoice invoice, java.io.File file) throws IOException {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
            writer.write("Счёт #" + invoice.getId());
            writer.write("\nКлиент: " + new ClientDAO().getClientById(invoice.getClientId()).getFullName());
            writer.write("\nМастер: " + new EmployeeDAO().getEmployeeById(invoice.getMasterId()).getFullName());
            writer.write("\nОбщая стоимость: " + invoice.getTotalAmount() + " руб.");

        }
    }
    
    public List<Request> getAllRequests() {
        return requestDAO.getAllRequests();
    }

    public Client getClientById(int clientId) {
        return clientDAO.getClientById(clientId);
    }

    public Request getRequestById(int requestId) {
        return requestDAO.getRequestById(requestId); 
    }
    
    
    
    
    
}
