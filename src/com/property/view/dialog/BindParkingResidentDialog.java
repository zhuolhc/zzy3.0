package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.ParkingSpace;
import com.property.model.Resident;
import com.property.dao.ParkingSpaceDAO;
import com.property.dao.ResidentDAO;
import java.util.List;

public class BindParkingResidentDialog extends JDialog {
    private boolean confirmed = false;
    private JComboBox<String> residentComboBox;
    private String spaceId;
    
    public BindParkingResidentDialog(JFrame parent, String spaceId) {
        super(parent, "绑定住户", true);
        this.spaceId = spaceId;
        initComponents();
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 住户选择下拉框
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("选择住户："), gbc);
        
        residentComboBox = new JComboBox<>();
        loadResidents();
        gbc.gridx = 1;
        mainPanel.add(residentComboBox, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                bindResident();
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void loadResidents() {
        ResidentDAO residentDAO = new ResidentDAO();
        List<Resident> residents = residentDAO.getAllResidents();
        
        residentComboBox.addItem("请选择住户");
        for (Resident resident : residents) {
            residentComboBox.addItem(resident.getResidentId() + " - " + resident.getName());
        }
    }
    
    private boolean validateInput() {
        if (residentComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "请选择要绑定的住户！");
            return false;
        }
        return true;
    }
    
    private void bindResident() {
        try {
            String selected = (String) residentComboBox.getSelectedItem();
            String residentId = selected.split(" - ")[0];
            
            ParkingSpaceDAO dao = new ParkingSpaceDAO();
            ParkingSpace space = dao.getParkingSpaceById(spaceId);
            
            if (space != null) {
                space.setResidentId(residentId);
                space.setStatus("已占用");
                dao.updateParkingSpace(space);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("绑定住户失败：" + e.getMessage());
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
} 