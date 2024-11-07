package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.model.Community;
import com.property.util.StyleUtil;

public class AddCommunityDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField nameField;
    private JSpinner buildingCountSpinner;
    private JSpinner unitCountSpinner;
    private JTextField addressField;
    private JTextArea descriptionArea;
    
    public AddCommunityDialog(JFrame parent) {
        super(parent, "添加小区", true);
        initComponents();
        setSize(400, 500);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 小区名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("小区名称："), gbc);
        
        nameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        
        // 楼宇总数
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("楼宇总数："), gbc);
        
        buildingCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridx = 1;
        mainPanel.add(buildingCountSpinner, gbc);
        
        // 单元总数
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("单元总数："), gbc);
        
        unitCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridx = 1;
        mainPanel.add(unitCountSpinner, gbc);
        
        // 地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("地址："), gbc);
        
        addressField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(addressField, gbc);
        
        // 描述
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("描述："), gbc);
        
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        mainPanel.add(scrollPane, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton confirmButton = new JButton("确认");
        StyleUtil.setButtonStyle(confirmButton, StyleUtil.SUCCESS_COLOR);
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });
        
        JButton cancelButton = new JButton("取消");
        StyleUtil.setButtonStyle(cancelButton, StyleUtil.DANGER_COLOR);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty() || 
            addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息！");
            return false;
        }
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Community getCommunity() {
        if (!confirmed) return null;
        
        return new Community(
            "C" + System.currentTimeMillis(),
            nameField.getText().trim(),
            (Integer) buildingCountSpinner.getValue(),
            (Integer) unitCountSpinner.getValue(),
            addressField.getText().trim(),
            descriptionArea.getText().trim(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
} 