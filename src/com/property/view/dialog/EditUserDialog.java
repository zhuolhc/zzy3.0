package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.User;
import com.property.util.StyleUtil;
import com.property.dao.UserDAO;

public class EditUserDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private User user;
    
    public EditUserDialog(JFrame parent, User user) {
        super(parent, "编辑用户", true);
        this.user = user;
        initComponents();
        loadUserData();
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
        
        roleComboBox = new JComboBox<>(new String[]{"管理员", "住户"});
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);
        
        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                updateUser();
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
    
    private void loadUserData() {
        if (user != null) {
            usernameField.setText(user.getUsername());
            roleComboBox.setSelectedItem(user.getRole());
        }
    }
    
    private boolean validateInput() {
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！");
            return false;
        }
        return true;
    }
    
    private void updateUser() {
        try {
            User updatedUser = new User(
                user.getUserId(),
                usernameField.getText().trim(),
                passwordField.getText().isEmpty() ? user.getPassword() : passwordField.getText(),
                (String) roleComboBox.getSelectedItem(),
                user.getResidentId(),
                "正常",  // 始终设置为正常状态
                user.getCreateTime()
            );
            
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(updatedUser);
            
            this.user = updatedUser;  // 更新当前用户对象
            confirmed = true;
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "保存用户信息时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public User getUser() {
        return user;
    }
} 