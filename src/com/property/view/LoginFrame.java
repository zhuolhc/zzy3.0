package com.property.view;

import javax.swing.*;
import java.awt.*;
import com.property.model.User;
import com.property.controller.UserController;
import com.property.util.StyleUtil;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("物业管理系统 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 添加标题
        JLabel titleLabel = new JLabel("物业管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // 用户名输入
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("用户名："), gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);
        
        // 密码输入
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("密码："), gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);
        
        // 登录按钮
        JButton loginButton = new JButton("登录");
        StyleUtil.setButtonStyle(loginButton, StyleUtil.PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> handleLogin());
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
            return;
        }
        
        UserController userController = new UserController();
        User user = userController.login(username, password);
        
        if (user != null) {
            if ("管理员".equals(user.getRole())) {
                AdminMainFrame adminFrame = new AdminMainFrame(user);
                adminFrame.setVisible(true);
                this.dispose();
            } else if ("住户".equals(user.getRole())) {
                ResidentMainFrame residentFrame = new ResidentMainFrame(user);
                residentFrame.setVisible(true);
                this.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！");
        }
    }
} 