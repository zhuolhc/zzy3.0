package com.property.view.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.property.model.ParkingSpace;
import com.property.model.Resident;
import com.property.dao.ParkingSpaceDAO;
import com.property.dao.ResidentDAO;
import com.property.util.StyleUtil;

public class ViewResidentParkingDialog extends JDialog {
    private JComboBox<String> residentComboBox;
    private DefaultTableModel parkingTableModel;
    private JTable parkingTable;
    
    public ViewResidentParkingDialog(JFrame parent) {
        super(parent, "查看住户车位", true);
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // 顶部面板 - 住户选择
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        topPanel.add(new JLabel("选择住户："));
        residentComboBox = new JComboBox<>();
        loadResidents();
        topPanel.add(residentComboBox);
        
        JButton searchButton = new JButton("查询");
        StyleUtil.setButtonStyle(searchButton, StyleUtil.PRIMARY_COLOR);
        searchButton.addActionListener(e -> searchParkingSpaces());
        topPanel.add(searchButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 中间面板 - 车位表格
        String[] columns = {"车位编号", "位置", "状态", "车牌号", "月租费用", "创建间"};
        parkingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        parkingTable = new JTable(parkingTableModel);
        JScrollPane scrollPane = new JScrollPane(parkingTable);
        
        // 设置表格样式
        parkingTable.setRowHeight(25);
        parkingTable.getTableHeader().setReorderingAllowed(false);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // 底部面板 - 关闭按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("关闭");
        StyleUtil.setButtonStyle(closeButton, StyleUtil.PRIMARY_COLOR);
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadResidents() {
        try {
            ResidentDAO residentDAO = new ResidentDAO();
            List<Resident> residents = residentDAO.getAllResidents();
            
            residentComboBox.addItem("请选择住户");
            for (Resident resident : residents) {
                residentComboBox.addItem(resident.getResidentId() + " - " + resident.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "加载住户数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchParkingSpaces() {
        if (residentComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "请选择要查询的住户！");
            return;
        }
        
        try {
            String selected = (String) residentComboBox.getSelectedItem();
            String residentId = selected.split(" - ")[0];
            
            ParkingSpaceDAO parkingDAO = new ParkingSpaceDAO();
            List<ParkingSpace> spaces = parkingDAO.getParkingSpacesByResidentId(residentId);
            
            // 清空表格
            parkingTableModel.setRowCount(0);
            
            // 填充数据
            for (ParkingSpace space : spaces) {
                parkingTableModel.addRow(new Object[]{
                    space.getSpaceId(),
                    space.getLocation(),
                    space.getStatus(),
                    space.getCarNumber(),
                    space.getMonthlyFee(),
                    space.getCreateTime()
                });
            }
            
            if (spaces.isEmpty()) {
                JOptionPane.showMessageDialog(this, "该住户没有绑定的车位！");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "查询车位数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 