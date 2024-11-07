package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.ParkingSpace;
import com.property.dao.ParkingSpaceDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSpaceDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JTextField carNumberField;
    private JTextField monthlyFeeField;
    private ParkingSpace parkingSpace;
    
    public ParkingSpaceDialog(JFrame parent, ParkingSpace space) {
        super(parent, space == null ? "添加车位" : "编辑车位", true);
        this.parkingSpace = space;
        initComponents();
        loadParkingSpaceData();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 位置
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("位置："), gbc);
        
        locationField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(locationField, gbc);
        
        // 状态
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("状态："), gbc);
        
        statusComboBox = new JComboBox<>(new String[]{"空闲", "已占用"});
        gbc.gridx = 1;
        mainPanel.add(statusComboBox, gbc);
        
        // 车牌号
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("车牌号："), gbc);
        
        carNumberField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(carNumberField, gbc);
        
        // 月租费用
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("月租费用："), gbc);
        
        monthlyFeeField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(monthlyFeeField, gbc);
        
        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                saveParkingSpace();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void loadParkingSpaceData() {
        if (parkingSpace != null) {
            locationField.setText(parkingSpace.getLocation());
            statusComboBox.setSelectedItem(parkingSpace.getStatus());
            carNumberField.setText(parkingSpace.getCarNumber());
            monthlyFeeField.setText(String.valueOf(parkingSpace.getMonthlyFee()));
        }
    }
    
    private boolean validateInput() {
        if (locationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入车位位置！");
            return false;
        }
        
        try {
            double monthlyFee = Double.parseDouble(monthlyFeeField.getText().trim());
            if (monthlyFee < 0) {
                JOptionPane.showMessageDialog(this, "月租费用不能为负数！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的月租费用！");
            return false;
        }
        
        return true;
    }
    
    private void saveParkingSpace() {
        try {
            ParkingSpaceDAO dao = new ParkingSpaceDAO();
            
            if (parkingSpace == null) {
                // 新增车位
                parkingSpace = new ParkingSpace(
                    dao.generateNewSpaceId(),
                    locationField.getText().trim(),
                    (String) statusComboBox.getSelectedItem(),
                    "",  // 新车位暂无绑定住户
                    carNumberField.getText().trim(),
                    Double.parseDouble(monthlyFeeField.getText().trim()),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                );
                dao.addParkingSpace(parkingSpace);
            } else {
                // 更新车位
                parkingSpace.setLocation(locationField.getText().trim());
                parkingSpace.setStatus((String) statusComboBox.getSelectedItem());
                parkingSpace.setCarNumber(carNumberField.getText().trim());
                parkingSpace.setMonthlyFee(Double.parseDouble(monthlyFeeField.getText().trim()));
                dao.updateParkingSpace(parkingSpace);
            }
            
            confirmed = true;
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "保存车位信息时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
} 