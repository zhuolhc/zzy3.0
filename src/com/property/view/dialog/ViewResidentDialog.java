package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.Resident;
import com.property.dao.ResidentDAO;

public class ViewResidentDialog extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField prepayAmountField;
    private JTextField arrearsAmountField;
    private Resident resident;
    private boolean editMode = false;
    
    public ViewResidentDialog(JFrame parent, String residentId) {
        super(parent, "住户资料", true);
        
        // 获取住户信息
        ResidentDAO residentDAO = new ResidentDAO();
        this.resident = residentDAO.getResidentById(residentId);
        
        if (this.resident == null) {
            throw new IllegalArgumentException("找不到指定的住户");
        }
        
        initComponents();
        loadResidentData();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 住户编号（只读）
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("住户编号："), gbc);
        
        JTextField idField = new JTextField(resident.getResidentId());
        idField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(idField, gbc);
        
        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("姓名："), gbc);
        
        nameField = new JTextField(20);
        nameField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        
        // 电话
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("电话："), gbc);
        
        phoneField = new JTextField(20);
        phoneField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // 地址
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("地址："), gbc);
        
        addressField = new JTextField(20);
        addressField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(addressField, gbc);
        
        // 预付金额
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("预付金额："), gbc);
        
        prepayAmountField = new JTextField(20);
        prepayAmountField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(prepayAmountField, gbc);
        
        // 欠费金额
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("欠费金额："), gbc);
        
        arrearsAmountField = new JTextField(20);
        arrearsAmountField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(arrearsAmountField, gbc);
        
        // 创建时间（只读）
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("创建时间："), gbc);
        
        JTextField createTimeField = new JTextField(resident.getCreateTime());
        createTimeField.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(createTimeField, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("编辑");
        JButton saveButton = new JButton("保存");
        JButton closeButton = new JButton("关闭");
        
        saveButton.setEnabled(false);
        
        editButton.addActionListener(e -> {
            editMode = true;
            toggleEditMode(true);
            editButton.setEnabled(false);
            saveButton.setEnabled(true);
        });
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                updateResident();
                editMode = false;
                toggleEditMode(false);
                editButton.setEnabled(true);
                saveButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "保存成功！");
            }
        });
        
        closeButton.addActionListener(e -> {
            if (editMode) {
                int result = JOptionPane.showConfirmDialog(this,
                    "有未保存的修改，确定要关闭吗？",
                    "确认关闭",
                    JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }
            } else {
                dispose();
            }
        });
        
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void loadResidentData() {
        nameField.setText(resident.getName());
        phoneField.setText(resident.getPhone());
        addressField.setText(resident.getAddress());
        prepayAmountField.setText(String.valueOf(resident.getPrepayAmount()));
        arrearsAmountField.setText(String.valueOf(resident.getArrearsAmount()));
    }
    
    private void toggleEditMode(boolean editable) {
        nameField.setEditable(editable);
        phoneField.setEditable(editable);
        addressField.setEditable(editable);
        prepayAmountField.setEditable(editable);
        arrearsAmountField.setEditable(editable);
    }
    
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入姓名！");
            return false;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入电话！");
            return false;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入地址！");
            return false;
        }
        
        try {
            double prepayAmount = Double.parseDouble(prepayAmountField.getText().trim());
            if (prepayAmount < 0) {
                JOptionPane.showMessageDialog(this, "预付金额不能为负数！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的预付金额！");
            return false;
        }
        
        try {
            double arrearsAmount = Double.parseDouble(arrearsAmountField.getText().trim());
            if (arrearsAmount < 0) {
                JOptionPane.showMessageDialog(this, "欠费金额不能为负数！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的欠费金额！");
            return false;
        }
        
        return true;
    }
    
    private void updateResident() {
        resident.setName(nameField.getText().trim());
        resident.setPhone(phoneField.getText().trim());
        resident.setAddress(addressField.getText().trim());
        resident.setPrepayAmount(Double.parseDouble(prepayAmountField.getText().trim()));
        resident.setArrearsAmount(Double.parseDouble(arrearsAmountField.getText().trim()));
        
        ResidentDAO residentDAO = new ResidentDAO();
        residentDAO.updateResident(resident);
    }
} 