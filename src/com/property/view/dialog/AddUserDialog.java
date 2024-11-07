package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.User;
import com.property.util.StyleUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddUserDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    
    public AddUserDialog(JFrame parent) {
        super(parent, "添加用户", true);
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("用户名："), gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);
        
        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("密码："), gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
        
        // 角色
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("角色："), gbc);
        
        String[] roles = {"管理员", "住户"};
        roleCombo = new JComboBox<>(roles);
        gbc.gridx = 1;
        mainPanel.add(roleCombo, gbc);
        
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
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private boolean validateInput() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息！");
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public User getUser() {
        if (!confirmed) return null;
        
        String role = roleCombo.getSelectedItem().equals("管理员") ? "ADMIN" : "USER";
        return new User(
            "U" + System.currentTimeMillis(),
            usernameField.getText().trim(),
            new String(passwordField.getPassword()).trim(),
            role,
            "正常",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
} 