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

/**
 * Класс который отвечает за всю бизнес-логику программы. Проводит все расчеты, меняет статусы заявки
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


   
    
    /**
     * Метод создающий ноую заявку для нового клиента
     * @param client Клиент, который будет указан в заявке
     * @param reason Причина, по которой клиент приехал в сервис(Поломка/Сервисное обслуживание)
     * @return 
     */
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
    
    /**
     * Метод который назначает Мастера-приёмщика для заявки
     * @param request Заявка, для которой назначается Приёмщик
     */
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
    

    
    
    /**
     * Метод который проводит диагностику. Меняет статус заявки на "Проведена диагностика"
     * @param request Заявка, для которой проводится диагностика 
     */
    public void conductDiagnostics2(Request request) {
        if (request == null || request.getRequestId() <= 0) throw new IllegalArgumentException("Некорректная заявка");
        if (request.getMasterId() == null) throw new IllegalStateException("Мастер-приемщик не назначен");
        if (!"Новая заявка".equalsIgnoreCase(request.getStatus())) throw new IllegalStateException("Диагностика проводится только для новых заявок");
        if ("сервисное обслуживание".equalsIgnoreCase(request.getReason())) throw new IllegalStateException("Диагностика не требуется для сервисного обслуживания");

        // 1. Сгенерировать найденные поломки (детали)
        CarPartsGenerator generator = new CarPartsGenerator();
        List<Part> breakdownParts = generator.generatePartsForServices();
        
        
        
        
        System.out.println("1. Сгенерированы поломки: ");
        if (breakdownParts.isEmpty()) {
            System.out.println("   - Нет поломок.");
        } else {
            for (int i = 0; i < breakdownParts.size(); i++) {
                Part part = breakdownParts.get(i);
                System.out.println("   - " + (i + 1) + ". " + part.getName() + " (Категория: " + part.getCategory() + ", Подкатегория: " + part.getSubcategory() + ", Цена: " + part.getPrice() + ")");
            }
        }
        
        
        
        

        // 2. Сохранить детали к заявке
        for (Part part : breakdownParts) {
            requestPartDAO.addRequestPart(new RequestPart(request.getRequestId(), part.getId()));
            System.out.println("   - Сохранена деталь: " + part.getName() + " (ID: " + part.getId() + ")");
        }

        // 3. Если поломок нет, сохранить результат и статус
        if (breakdownParts.isEmpty()) {
            System.out.println("Поломок нет");
            request.setDiagnosticResult("Неисправностей не обнаружено.");
            request.setStatus("Проведена диагностика");
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
                    
                    System.out.println("   - Привязана услуга: " + serv.getCategory() + " (Подкатегория: " + serv.getSubcategory() + ", Цена: " + serv.getPrice() + ", Роль мастера: " + serv.getRequiredMechanicRole() + ")");
                    
                    break;
                }
            }
        }



        // 6. Сохраняем результат и статус
        request.setDiagnosticResult("Обнаружены неисправности"); 
        request.setStatus("Проведена диагностика");
        if (!requestDAO.updateRequest(request)) {
            throw new RuntimeException("Не удалось обновить заявку #" + request.getRequestId());
        }
    }
    
    
    
    
    /**
     * Метод, который назначает именно автослесаря для ремонта 
     * @param request Заявка, для которой назначают автослесаря
     */
    public void assignMechanic(Request request) {
        if (request == null || request.getRequestId() <= 0) throw new IllegalArgumentException("Некорректная заявка");
        if (!"Проведена диагностика".equalsIgnoreCase(request.getStatus())) throw new IllegalStateException("Назначение механиков возможно только после диагностики");
        System.out.println("=== Начало назначения механиков для заявки #" + request.getRequestId() + " ===");
        
        
        // Проверка, назначены ли уже механики
        List<Integer> existingMechanics = requestMechanicsDAO.getMechanicsForRequest(request.getRequestId());
        if (!existingMechanics.isEmpty()) {
            System.out.println("   - Механики уже назначены: " + existingMechanics.size() + " шт.");
            for (Integer mechId : existingMechanics) {
                Employee mechanic = employeeDAO.getEmployeeById(mechId);
                if (mechanic != null) {
                    System.out.println("   - Назначен: " + mechanic.getFullName());
                }
            }
            return; // Прекращаем выполнение, если механики уже есть
        }
        

        List<RequestService> requestServices = requestServiceDAO.getRequestServicesByRequestId(request.getRequestId());
        if (requestServices.isEmpty()) {
            System.out.println("   - Нет привязанных услуг, механики не требуются.");
            return;
        }

        Set<String> assignedRoles = new HashSet<>();
        for (RequestService rs : requestServices) {
            Service service = serviceDAO.getServiceById(rs.getServiceId());
            String mechanicRole = service.getRequiredMechanicRole();
            if (!assignedRoles.contains(mechanicRole)) {
                List<Employee> mechanics = employeeDAO.getEmployeesByRole(mechanicRole);
                if (!mechanics.isEmpty()) {
                    Employee mechanic = mechanics.get(random.nextInt(mechanics.size()));
                    requestMechanicsDAO.addMechanicToRequest(request.getRequestId(), mechanic.getId());
                    System.out.println("   - Назначен мастер: " + mechanic.getFullName() + " (Роль: " + mechanicRole + ")");
                } else {
                    System.out.println("   - Нет доступных мастеров для роли: " + mechanicRole);
                }
                assignedRoles.add(mechanicRole);
            }
        }
        System.out.println("=== Назначение механиков завершено ===");
    }
    
    
    
    
    /**
     * Метод, который проводит Сервисное обслуживание. Меняет статус заявки на "Проведено обслуживание"
     * @param request Заявка, для которой проводится Сервисное обслуживание 
     */
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
        request.setStatus("Проведено обслуживание");
        request.setWorkResult("Работы выполнены");
        requestDAO.updateRequest(request);
    }
    

    /**
     * Метод, который проводит ремонт после диагностики. Меняет статус заявки на "Проведена работа"
     * @param request Заявка, для которой проводится ремонт 
     */
    public void performWork(Request request) {
        if (request == null || request.getRequestId() <= 0) {
            throw new IllegalArgumentException("Заявка недействительна");
        }
        if (!"Проведена диагностика".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Ремонт можно проводить только после диагностики.");
        }

        // Проверка наличия назначенных автослесарей
        RequestMechanicsDAO mechanicsDAO = new RequestMechanicsDAO();
        List<Integer> mechanicIds = mechanicsDAO.getMechanicsForRequest(request.getRequestId());
        if (mechanicIds == null || mechanicIds.isEmpty()) {
            throw new IllegalStateException("Для ремонта должен быть назначен хотя бы один автослесарь.");
        }
        
        System.out.println("=== Начало проведения работы для заявки # " + request.getRequestId() + " ===");

        request.setStatus("Проведена работа");
        request.setWorkResult("Работы выполнены");
        boolean updated = requestDAO.updateRequest(request);
        if (!updated) {
            throw new RuntimeException("Не удалось обновить статус заявки на ремонт с ID: " + request.getRequestId());
        }
        
        System.out.println("=== Работа завершена ===");
    }
    
    /**
     * Метод, который рассчитывает стоимость всех работ, выставляет счет для заявки 
     * @param request Заявка, для которой выставляется счет 
     * @return возвращает заявку
     * @throws IOException 
     */
    public Invoice viewInvoice(Request request) throws IOException {
        if (request == null || request.getRequestId() <= 0) {
            throw new IllegalArgumentException("Заявка недействительна");
        }
        if (!"Проведена работа".equalsIgnoreCase(request.getStatus()) && !("Проведено обслуживание".equalsIgnoreCase(request.getStatus()))  && !"Отказ от ремонта".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Завершение возможно только для заявок в статусе 'Ремонт' или 'Диагностика' с причиной 'сервисное обслуживание'.");
        }
        
        double totalCost = 0.0;
         InvoiceDAO invoiceDAO = new InvoiceDAO();
        if ("Отказ от ремонта".equalsIgnoreCase(request.getStatus())) {
            Service diagService = serviceDAO.getServiceByCategory("Диагностика");
            totalCost = diagService.getPrice();
            System.out.println("Цена диагностики: " + totalCost);
            Invoice invoice = new Invoice (
                    request.getRequestId(),
                    request.getClientId(),
                    request.getMasterId(),
                    (int)totalCost
            );
            invoiceDAO.addInvoice(invoice);
            return invoice;
        }
        
        
        System.out.println("=== Просмтор счета для заявки #" + request.getRequestId() + " ===");
        

        // 1. Расчёт общей стоимости
        
        List<RequestPart> requestParts = requestPartDAO.getRequestPartsByRequestId(request.getRequestId());
        List<RequestService> requestServices = requestServiceDAO.getRequestServicesByRequestId(request.getRequestId());
        System.out.println("Детали: ");
        for (RequestPart rp : requestParts) {
            Part part = new PartDAO().getPartById(rp.getPartId());
            if (part != null) {
                totalCost += part.getPrice();
                System.out.println("   - " + part.getName() + " (Цена: " + part.getPrice() + ")");
            }
        }
        
        System.out.println("Услуги: ");
        for (RequestService rs : requestServices) {
            Service service = new ServiceDAO().getServiceById(rs.getServiceId());
            if (service != null) {
                totalCost += service.getPrice();
                System.out.println("   - " + service.getCategory() + ": " + service.getSubcategory() + "Цена: " + service.getPrice());
            }
        }
        
        System.out.println("Диагностика: ");
        if (request.getStatus().equalsIgnoreCase("Проведена работа")) {
            Service service = new ServiceDAO().getServiceByCategory("Диагностика");
            totalCost += service.getPrice();
            System.out.println(" - "+ service.getCategory() + ": " + "Цена: " + service.getPrice());
        }
        
        System.out.println("Общая стоимость: " + totalCost);
        


        // 3. Создание счёта
        invoiceDAO = new InvoiceDAO();
        Invoice invoice = new Invoice(request.getRequestId(), request.getClientId(), request.getMasterId(), (int) totalCost);
        int invoiceId = invoiceDAO.addInvoice(invoice);
        System.out.println("Счёт создан. ID: " + invoiceId);
        System.out.println("===================");
        
        return invoice;
    }
    

    /**
     * Метод который делает txt файл со счётом
     * @param invoice Счет для которого делается txt файла 
     * @param file
     * @throws IOException 
     */
    public void saveInvoiceToTxt(Invoice invoice, java.io.File file) throws IOException {
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
    
    
    
    public void declineRepair(Request request) {
        if (!"Проведена диагностика".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Отказ возможен только после проведённой диагностики.");
        }

        request.setStatus("Отказ от ремонта");
        request.setWorkResult("Клиент отказался от ремонта");
        requestDAO.updateRequest(request);

        Service diagService = serviceDAO.getServiceByCategory("Диагностика");
        if (diagService == null) {
            throw new RuntimeException("Услуга 'Диагностика' не найдена в базе");
        }

        Invoice invoice = new Invoice(
            request.getRequestId(),
            request.getClientId(),
            request.getMasterId(),
            (int) diagService.getPrice()
        );
        new InvoiceDAO().addInvoice(invoice);

        System.out.println("Клиент отказался от ремонта. Выставлен счёт только за услугу 'Диагностика'.");
    }


    
    
    
    
}
