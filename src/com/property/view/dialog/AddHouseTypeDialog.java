package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.model.HouseType;
import com.property.dao.HouseTypeDAO;

public class AddHouseTypeDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField typeNameField;
    private JTextField areaField;
    private JTextArea descriptionArea;
    
    public AddHouseTypeDialog(JFrame parent) {
        super(parent, "添加房型", true);
        initComponents();
        setSize(400, 400);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 房型名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("房型名称："), gbc);
        
        typeNameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(typeNameField, gbc);
        
        // 建筑面积
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("建筑面积："), gbc);
        
        areaField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(areaField, gbc);
        
        // 描述
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("描述："), gbc);
        
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        mainPanel.add(scrollPane, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                saveHouseType();
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private boolean validateInput() {
        if (typeNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入房型名称！");
            return false;
        }
        
        try {
            double area = Double.parseDouble(areaField.getText().trim());
            if (area <= 0) {
                JOptionPane.showMessageDialog(this, "建筑面积必须大于0！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的建筑面积！");
            return false;
        }
        
        return true;
    }
    
    private void saveHouseType() {
        HouseTypeDAO houseTypeDAO = new HouseTypeDAO();
        String typeId = houseTypeDAO.generateNewTypeId();
        
        HouseType houseType = new HouseType(
            typeId,
            typeNameField.getText().trim(),
            Double.parseDouble(areaField.getText().trim()),
            descriptionArea.getText().trim(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        
        houseTypeDAO.addHouseType(houseType);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
} 