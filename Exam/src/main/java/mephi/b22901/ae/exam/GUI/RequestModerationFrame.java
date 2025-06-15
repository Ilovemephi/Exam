/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.exam.GUI;



import mephi.b22901.ae.exam.*;
import mephi.b22901.ae.exam.DAO.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import mephi.b22901.ae.exam.Logic.CarServiceLogic;

public class RequestModerationFrame extends JFrame {

    private final Request request;
    private final Client client;

    private final ClientDAO clientDAO = new ClientDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private JLabel masterLabel = new JLabel("-");
    private JLabel mechanicsLabel = new JLabel("-");
    private JLabel resultLabel = new JLabel("-");
    private JLabel statusLabel = new JLabel("-");

    public RequestModerationFrame(Request request) {
        this.request = request;
        this.client = clientDAO.getClientById(request.getClientId());

        setTitle("Модерация заявки #" + request.getRequestId());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel infoPanel = buildInfoPanel();
        JPanel buttonPanel = buildButtonPanel();

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("Имя клиента:"));
        panel.add(new JLabel(client.getFullName()));

        panel.add(new JLabel("Телефон:"));
        panel.add(new JLabel(client.getPhoneNumber()));

        panel.add(new JLabel("Причина обращения:"));
        panel.add(new JLabel(request.getReason()));

        panel.add(new JLabel("Статус:"));
        statusLabel.setText(request.getStatus());
        panel.add(statusLabel);

        if (request.getMasterId() != null) {
            Employee master = employeeDAO.getEmployeeById(request.getMasterId());
            panel.add(new JLabel("Мастер-приёмщик:"));
            masterLabel.setText(master.getFullName());
            panel.add(masterLabel);
        }

        RequestMechanicsDAO rmDAO = new RequestMechanicsDAO();
        List<Integer> mechanicIds = rmDAO.getMechanicsForRequest(request.getRequestId());
        if (!mechanicIds.isEmpty()) {
            panel.add(new JLabel("Автослесарь(и):"));
            String mechanicNames = "";
            for (int i = 0; i < mechanicIds.size(); i++) {
                Employee mech = employeeDAO.getEmployeeById(mechanicIds.get(i));
                mechanicNames += mech.getFullName();
                if (i != mechanicIds.size() - 1) {
                    mechanicNames += "; ";
                }
            }
            mechanicsLabel.setText(mechanicNames);
            panel.add(mechanicsLabel);
        }

        panel.add(new JLabel("Результат работы:"));
        String result = request.getWorkResult();
        resultLabel.setText(result != null ? result : "-");
        panel.add(resultLabel);

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        String status = request.getStatus();

        if ("Новая заявка".equalsIgnoreCase(status)) {
            JButton assignMasterBtn = new JButton("Назначить приёмщика");
            assignMasterBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    assignMaster();
                }
            });
            panel.add(assignMasterBtn);

            JButton startDiagBtn = new JButton("Провести диагностику");
            startDiagBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    conductDiagnostics();
                }
            });
            if (!"Сервисное обслуживание".equalsIgnoreCase(request.getReason())) {
                panel.add(startDiagBtn);
            }
            if ("Сервисное обслуживание".equalsIgnoreCase(request.getReason())) {
            JButton conductMaintenanceBtn = new JButton("Провести сервисное обслуживание");
            conductMaintenanceBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    conductMaintenance();
                }
            });
            panel.add(conductMaintenanceBtn);
        }
        } else if ("Проведена диагностика".equalsIgnoreCase(request.getStatus())) {
            
            RequestMechanicsDAO rmDAO = new RequestMechanicsDAO();
            List<Integer> mechanicIds = rmDAO.getMechanicsForRequest(request.getRequestId());
            if (mechanicIds.isEmpty()) {
    
            JButton assignMechanicBtn = new JButton("Назначить автослесаря");
            assignMechanicBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    assignMechanic();
                }
            });
            panel.add(assignMechanicBtn);
            
            }
            
            if (!"Проведена работа".equalsIgnoreCase(status)){
                if (!mechanicIds.isEmpty()){
                    JButton doWorkBtn = new JButton("Провести работу");
                    doWorkBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            performWork();
                        }
                    });
                    panel.add(doWorkBtn);
                }
            }
        } else if ("Проведена работа".equalsIgnoreCase(status) || "Проведено обслуживание".equalsIgnoreCase(status)) {
            JButton viewInvoiceBtn = new JButton("Просмотреть счёт");
            viewInvoiceBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewInvoice();
                }
            });
            panel.add(viewInvoiceBtn);
        }

        return panel;
    }

    private void assignMaster() {
        if (request.getMasterId() != null) {
            JOptionPane.showMessageDialog(this, "Мастер уже назначен!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } else{
        CarServiceLogic logic = new CarServiceLogic();
        logic.assignMaster(request);
        JOptionPane.showMessageDialog(this, "Мастер назначен");
        dispose(); 
        }
    }

    private void conductDiagnostics() {
        CarServiceLogic logic = new CarServiceLogic();
        try {
            logic.conductDiagnostics2(request);
            JOptionPane.showMessageDialog(this, "Диагностика завершена");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
   private void conductMaintenance() {
       CarServiceLogic logic = new CarServiceLogic();
       try {
           logic.conductMaintenance(request);
           JOptionPane.showMessageDialog(this, "Сервисное обслуживание проведено");
           dispose(); 
       } catch(Exception ex) {
           JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
       }
   }

    private void assignMechanic() {
        CarServiceLogic logic = new CarServiceLogic();
        logic.assignMechanic(request); 
        JOptionPane.showMessageDialog(this, "Автослесарь назначен");
        dispose();
    }

    private void performWork() {
        CarServiceLogic logic = new CarServiceLogic();
        logic.performWork(request); 
        JOptionPane.showMessageDialog(this, "Работа проведена");
        dispose();
    }

    private void viewInvoice() {
         CarServiceLogic logic = new CarServiceLogic(); // Создаём экземпляр логики (пока так, позже оптимизируем)
        try {
            new InvoiceFrame(logic, request, client, employeeDAO).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Произошла ошибка при просмотре счёта", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
}
