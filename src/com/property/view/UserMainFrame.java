package com.property.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.model.User;
import com.property.model.Repair;
import com.property.controller.UserController;
import com.property.dao.RepairDAO;
import com.property.util.StyleUtil;
import com.property.controller.DataUpdateController;
import com.property.model.Complaint;
import com.property.dao.ComplaintDAO;
import java.util.List;
import com.property.view.dialog.AddComplaintDialog;
import com.property.view.dialog.ViewComplaintDialog;

public class UserMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private DefaultTableModel complaintTableModel;
    private JTable complaintTable;
    
    public UserMainFrame(User user) {
        this.currentUser = user;
        setTitle("用户界面 - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建顶部面板
        createTopPanel(mainPanel);
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("个人信息", createPersonalInfoPanel());
        tabbedPane.addTab("在线报修", createRepairPanel());
        tabbedPane.addTab("投诉管理", createComplaintPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void createTopPanel(JPanel mainPanel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // 左侧欢迎信息
        JLabel welcomeLabel = new JLabel("欢迎您，" + currentUser.getUsername());
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        
        // 右侧按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // 退出按钮
        JButton logoutButton = new JButton("退出登录");
        StyleUtil.setButtonStyle(logoutButton, StyleUtil.DANGER_COLOR);
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(logoutButton);
        
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户名："), gbc);
        
        JTextField usernameField = new JTextField(20);
        usernameField.setText(currentUser.getUsername());
        usernameField.setEditable(false);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("新密码："), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // 确认密码
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("确认密码："), gbc);
        
        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        // 钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton saveButton = new JButton("保存修改");
        StyleUtil.setButtonStyle(saveButton, StyleUtil.SUCCESS_COLOR);
        saveButton.addActionListener(e -> {
            String newPassword = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入新密码！");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的密码不一致！");
                return;
            }
            
            // 更新密码
            currentUser.setPassword(newPassword);
            UserController userController = new UserController();
            userController.updateUser(currentUser);
            
            JOptionPane.showMessageDialog(this, "密码修改成功！");
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            // 通知数据更新
            DataUpdateController.notifyDataChanged("USER_INFO");
        });
        
        buttonPanel.add(saveButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createRepairPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 报修类型
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("报修类型："), gbc);
        
        String[] repairTypes = {"水电维修", "门窗维修", "空调维修", "其他"};
        JComboBox<String> typeCombo = new JComboBox<>(repairTypes);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);
        
        // 报修描述
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("报修描述："), gbc);
        
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descArea);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);
        
        // 联系方式
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("联系方式："), gbc);
        
        JTextField contactField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(contactField, gbc);
        
        // 提交按钮
        JButton submitButton = new JButton("提交报修");
        StyleUtil.setButtonStyle(submitButton, StyleUtil.PRIMARY_COLOR);
        submitButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String description = descArea.getText().trim();
            String contact = contactField.getText().trim();
            
            if (description.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }
            
            // 创建报修记录
            Repair repair = new Repair(
                "R" + System.currentTimeMillis(),
                currentUser.getUserId(),
                currentUser.getUsername(),
                type,
                description,
                contact,
                "待处理",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            
            // 保存报修记录
            RepairDAO repairDAO = new RepairDAO();
            repairDAO.addRepair(repair);
            
            JOptionPane.showMessageDialog(this, "报修提交成功！");
            
            // 清空表单
            typeCombo.setSelectedIndex(0);
            descArea.setText("");
            contactField.setText("");
        });
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);
        
        return panel;
    }
    
    private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("提交投诉");
        JButton viewButton = new JButton("查看详情");
        
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        StyleUtil.setButtonStyle(viewButton, StyleUtil.PRIMARY_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(viewButton);
        
        // 表格
        String[] columns = {"投诉编号", "类型", "标题", "状态", "提交时间", "处理时间", "处理结果"};
        complaintTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        complaintTable = new JTable(complaintTableModel);
        JScrollPane scrollPane = new JScrollPane(complaintTable);
        
        // 设置表格样式
        complaintTable.setRowHeight(25);
        complaintTable.getTableHeader().setReorderingAllowed(false);
        
        // 添加投诉按钮事件
        addButton.addActionListener(e -> {
            AddComplaintDialog dialog = new AddComplaintDialog(this, currentUser);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                refreshComplaintData();
            }
        });
        
        // 查看详情按钮事件
        viewButton.addActionListener(e -> {
            int selectedRow = complaintTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要查看的投诉！");
                return;
            }
            
            String complaintId = (String) complaintTable.getValueAt(selectedRow, 0);
            ViewComplaintDialog dialog = new ViewComplaintDialog(this, complaintId);
            dialog.setVisible(true);
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 初始加载数据
        refreshComplaintData();
        
        return panel;
    }
    
    private void refreshComplaintData() {
        try {
            ComplaintDAO complaintDAO = new ComplaintDAO();
            List<Complaint> complaints = complaintDAO.getComplaintsByUserId(currentUser.getUserId());
            
            complaintTableModel.setRowCount(0);
            for (Complaint complaint : complaints) {
                complaintTableModel.addRow(new Object[]{
                    complaint.getComplaintId(),
                    complaint.getType(),
                    complaint.getTitle(),
                    complaint.getStatus(),
                    complaint.getCreateTime(),
                    complaint.getReply(),
                    complaint.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新投诉数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "确定要退出登录吗？",
            "确认退出",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
} 