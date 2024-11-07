package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.User;
import com.property.model.Complaint;
import com.property.dao.ComplaintDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddComplaintDialog extends JDialog {
    private boolean confirmed = false;
    private User currentUser;
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> typeComboBox;
    private JTextField contactField;
    
    public AddComplaintDialog(JFrame parent, User currentUser) {
        super(parent, "提交投诉", true);
        this.currentUser = currentUser;
        initComponents();
        setSize(400, 500);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 投诉类型
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("投诉类型："), gbc);
        
        String[] types = {"物业服务", "设施维修", "安全问题", "环境卫生", "其他"};
        typeComboBox = new JComboBox<>(types);
        gbc.gridx = 1;
        mainPanel.add(typeComboBox, gbc);
        
        // 投诉标题
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("投诉标题："), gbc);
        
        titleField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(titleField, gbc);
        
        // 投诉内容
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("投诉内容："), gbc);
        
        contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        gbc.gridx = 1;
        mainPanel.add(scrollPane, gbc);
        
        // 联系方式
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("联系方式："), gbc);
        
        contactField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(contactField, gbc);
        
        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("提交");
        JButton cancelButton = new JButton("取消");
        
        submitButton.addActionListener(e -> {
            if (validateInput()) {
                submitComplaint();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入投诉标题！");
            return false;
        }
        
        if (contentArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入投诉内容！");
            return false;
        }
        
        if (contactField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入联系方式！");
            return false;
        }
        
        return true;
    }
    
    private void submitComplaint() {
        try {
            ComplaintDAO dao = new ComplaintDAO();
            String complaintId = dao.generateNewComplaintId();
            String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            Complaint complaint = new Complaint(
                complaintId,
                currentUser.getUserId(),
                currentUser.getUsername(),
                typeComboBox.getSelectedItem().toString(),
                titleField.getText().trim(),
                contentArea.getText().trim(),
                contactField.getText().trim(),
                "待处理",
                "",  // reply
                currentTime
            );
            
            dao.addComplaint(complaint);
            confirmed = true;
            dispose();
            JOptionPane.showMessageDialog(this, "投诉提交成功！");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "提交投诉时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
} 