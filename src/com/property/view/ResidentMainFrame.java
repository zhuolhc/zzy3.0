package com.property.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.model.User;
import com.property.model.Resident;
import com.property.model.Complaint;
import com.property.model.Repair;
import com.property.dao.ResidentDAO;
import com.property.dao.ComplaintDAO;
import com.property.dao.RepairDAO;
import com.property.util.StyleUtil;
import com.property.view.dialog.AddComplaintDialog;
import com.property.view.dialog.ViewComplaintDialog;

public class ResidentMainFrame extends JFrame {
    private User user;
    private Resident resident;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private DefaultTableModel complaintTableModel;
    private JTable complaintTable;
    
    public ResidentMainFrame(User user) {
        this.user = user;
        loadResidentData();
        initComponents();
        
        setTitle("物业管理系统 - 住户界面");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void loadResidentData() {
        if (user.getResidentId() != null && !user.getResidentId().isEmpty()) {
            ResidentDAO residentDAO = new ResidentDAO();
            resident = residentDAO.getResidentById(user.getResidentId());
        }
    }
    
    private void initComponents() {
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout());
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("个人信息", createPersonalInfoPanel());
        tabbedPane.addTab("报修管理", createRepairPanel());
        tabbedPane.addTab("投诉管理", createComplaintPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 添加个人信息字段
        int row = 0;
        
        // 用户名
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("用户名："), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(user.getUsername()), gbc);
        row++;
        
        if (resident != null) {
            // 姓名
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(new JLabel("姓名："), gbc);
            gbc.gridx = 1;
            panel.add(new JLabel(resident.getName()), gbc);
            row++;
            
            // 联系电话
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(new JLabel("联系电话："), gbc);
            gbc.gridx = 1;
            panel.add(new JLabel(resident.getPhone()), gbc);
            row++;
            
            // 房屋地址
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(new JLabel("房屋地址："), gbc);
            gbc.gridx = 1;
            panel.add(new JLabel(resident.getAddress()), gbc);
            row++;
        }
        
        return panel;
    }
    
    private JPanel createRepairPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 报修类型
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("报修类型："), gbc);
        
        String[] types = {"水电维修", "门窗维修", "空调维修", "其他"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);
        
        // 报修描述
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("报修描述："), gbc);
        
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descArea);
        gbc.gridx = 1;
        formPanel.add(scrollPane, gbc);
        
        // 联系方式
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("联系方式："), gbc);
        
        JTextField contactField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);
        
        // 提交按钮
        JButton submitButton = new JButton("提交报修");
        StyleUtil.setButtonStyle(submitButton, StyleUtil.PRIMARY_COLOR);
        
        submitButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String description = descArea.getText().trim();
            String contact = contactField.getText().trim();
            
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入报修描述！");
                return;
            }
            
            if (contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入联系方式！");
                return;
            }
            
            // 创建报修记录
            Repair repair = new Repair(
                "R" + System.currentTimeMillis(),
                user.getUserId(),
                user.getUsername(),
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
        formPanel.add(submitButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
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
        String[] columns = {"投诉编号", "类型", "标题", "状态", "提交时间", "回复内容"};
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
            AddComplaintDialog dialog = new AddComplaintDialog(this, user);
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
            List<Complaint> complaints = complaintDAO.getComplaintsByUserId(user.getUserId());
            
            complaintTableModel.setRowCount(0);
            for (Complaint complaint : complaints) {
                complaintTableModel.addRow(new Object[]{
                    complaint.getComplaintId(),
                    complaint.getType(),
                    complaint.getTitle(),
                    complaint.getStatus(),
                    complaint.getCreateTime(),
                    complaint.getReply()
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
} 