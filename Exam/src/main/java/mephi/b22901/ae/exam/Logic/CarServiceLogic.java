/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.Logic;

import java.util.Random;
import mephi.b22901.ae.exam.Client;
import mephi.b22901.ae.exam.DAO.ClientDAO;
import mephi.b22901.ae.exam.DAO.RequestDAO;
import mephi.b22901.ae.exam.Request;

/**
 *
 * @author artyom_egorkin
 */
public class CarServiceLogic {
    
    private final ClientDAO clientDAO = new ClientDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final Random random = new Random();

    
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
    
    
    
    
    
    
}
