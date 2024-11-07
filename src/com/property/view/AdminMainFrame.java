package com.property.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.property.model.User;
import com.property.model.Repair;
import com.property.controller.UserController;
import com.property.controller.DataUpdateController;
import com.property.dao.UserDAO;
import com.property.dao.RepairDAO;
import com.property.util.StyleUtil;
import com.property.view.dialog.AddUserDialog;
import com.property.view.dialog.EditUserDialog;
import com.property.model.Complaint;
import com.property.dao.ComplaintDAO;
import com.property.model.Community;
import com.property.dao.CommunityDAO;
import com.property.model.HouseType;
import com.property.dao.HouseTypeDAO;
import com.property.view.dialog.AddHouseTypeDialog;
import com.property.view.dialog.EditHouseTypeDialog;
import com.property.model.Resident;
import com.property.dao.ResidentDAO;
import com.property.view.dialog.AddResidentDialog;
import com.property.view.dialog.EditResidentDialog;
import com.property.view.dialog.BindResidentDialog;
import com.property.view.dialog.ViewResidentDialog;
import com.property.model.ParkingSpace;
import com.property.dao.ParkingSpaceDAO;
import com.property.view.dialog.ParkingSpaceDialog;
import com.property.view.dialog.BindParkingResidentDialog;
import com.property.view.dialog.ViewResidentParkingDialog;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.dao.FeeItemDAO;
import com.property.model.FeeItem;
import com.property.util.FileUtil;

public class AdminMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    
    // 添加表格作为成员变量
    private JTable userTable;
    private JTable repairTable;
    private JTable complaintTable;
    private JTable houseTypeTable;
    private JTable communityTable;
    private JTable residentTable;
    
    public AdminMainFrame(User user) {
        this.currentUser = user;
        
        // 初始化数据文件
        FileUtil.initializeFiles();
        
        setTitle("管理员界面 - " + user.getUsername());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建顶部面板
        createTopPanel(mainPanel);
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("小区信息", createCommunityPanel());
        tabbedPane.addTab("用户管理", createUserManagementPanel());
        tabbedPane.addTab("报修管理", createRepairManagementPanel());
        tabbedPane.addTab("投诉管理", createComplaintManagementPanel());
        tabbedPane.addTab("房型管理", createHouseTypeManagementPanel());
        tabbedPane.addTab("住户管理", createResidentManagementPanel());
        tabbedPane.addTab("车位管理", createParkingManagementPanel());
        tabbedPane.addTab("收费管理", createFeeManagementPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
        
        // 注册数据更新监听器
        DataUpdateController.addListener(new DataUpdateController.DataUpdateListener() {
            @Override
            public void onDataChanged(String dataType) {
                switch (dataType) {
                    case "USER_INFO":
                        refreshUserData();
                        break;
                    case "REPAIR":
                        refreshRepairData();
                        break;
                    case "COMPLAINT":
                        refreshComplaintData();
                        break;
                    case "HOUSE_TYPE":
                        refreshHouseTypeData();
                        break;
                }
            }
        });
    }
    
    // 创建用户管理面板
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加用户");
        JButton editButton = new JButton("编辑用户");
        JButton deleteButton = new JButton("删除用户");
        JButton bindResidentButton = new JButton("绑定住户");
        JButton viewResidentButton = new JButton("查看住户资料");
        
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        StyleUtil.setButtonStyle(editButton, StyleUtil.PRIMARY_COLOR);
        StyleUtil.setButtonStyle(deleteButton, StyleUtil.DANGER_COLOR);
        StyleUtil.setButtonStyle(bindResidentButton, StyleUtil.WARNING_COLOR);
        StyleUtil.setButtonStyle(viewResidentButton, StyleUtil.INFO_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(bindResidentButton);
        toolBar.add(viewResidentButton);
        
        // 表格
        String[] columns = {"用户ID", "用户名", "角色", "绑定住户", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // 绑定住户按钮事件
        bindResidentButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要绑定的用户！");
                return;
            }
            
            String role = (String) userTable.getValueAt(selectedRow, 2);
            String userId = (String) userTable.getValueAt(selectedRow, 0);
            
            System.out.println("选中用户ID: " + userId);
            System.out.println("用户角色: " + role);
            
            if (!"住户".equals(role)) {
                System.out.println("角色不匹配，当前角色: " + role);
                JOptionPane.showMessageDialog(this, "只能为住户角色绑定住户资料！");
                return;
            }
            
            try {
                BindResidentDialog dialog = new BindResidentDialog(this, userId);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshUserData(model);
                    JOptionPane.showMessageDialog(this, "住户绑定成功！");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "绑定住户时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 查看住户资料按钮事件
        viewResidentButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要查看的用户！");
                return;
            }
            
            String residentId = getUserResidentId(selectedRow);
            if (residentId == null || residentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "该用户未绑定住户！");
                return;
            }
            
            try {
                ViewResidentDialog dialog = new ViewResidentDialog(this, residentId);
                dialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "查看住户资料时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 添加按钮事件
        addButton.addActionListener(e -> {
            AddUserDialog dialog = new AddUserDialog(this);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                User newUser = dialog.getUser();
                if (newUser != null) {
                    UserController userController = new UserController();
                    userController.addUser(newUser);
                    refreshUserData();
                    JOptionPane.showMessageDialog(this, "用户添加成功！");
                }
            }
        });
        
        // 编辑按钮事件
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要编辑的用户！");
                return;
            }
            
            String userId = (String) userTable.getValueAt(selectedRow, 0);
            
            try {
                UserDAO userDAO = new UserDAO();
                User selectedUser = userDAO.getUserById(userId);
                
                if (selectedUser != null) {
                    EditUserDialog dialog = new EditUserDialog(this, selectedUser);
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
                        refreshUserData(model);
                        JOptionPane.showMessageDialog(this, "用户信息更新成功！");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "找不到选中的用户！");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "编辑用户时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的用户！");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除该用户吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                String userId = (String) userTable.getValueAt(selectedRow, 0);
                UserController userController = new UserController();
                userController.deleteUser(userId);
                refreshUserData();
                JOptionPane.showMessageDialog(this, "用户删除成功！");
            }
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // 创建报修管理面板
    private JPanel createRepairManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 创建工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton handleButton = new JButton("处理报修");
        StyleUtil.setButtonStyle(handleButton, StyleUtil.INFO_COLOR);
        toolBar.add(handleButton);
        
        // 创建报修表格
        String[] columns = {"报修编号", "报修人", "类型", "描述", "联系方式", "状态", "提交时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        repairTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(repairTable);
        
        // 加载报修数据
        loadRepairData(model);
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // 创建投诉管理面板
    private JPanel createComplaintManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton replyButton = new JButton("回复投诉");
        StyleUtil.setButtonStyle(replyButton, StyleUtil.WARNING_COLOR);
        toolBar.add(replyButton);
        
        // 创建投诉表格
        String[] columns = {"投诉编号", "投诉人", "类型", "标题", "内容", "联系方式", "态", "回复", "提时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable complaintTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(complaintTable);
        
        // 加载投诉数据
        loadComplaintData(model);
        
        // 添加回复按钮事件
        replyButton.addActionListener(e -> {
            int selectedRow = complaintTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要回复的投诉！");
                return;
            }
            
            String complaintId = (String) complaintTable.getValueAt(selectedRow, 0);
            String status = (String) complaintTable.getValueAt(selectedRow, 6);
            
            if ("已回复".equals(status)) {
                JOptionPane.showMessageDialog(this, "该投诉已经回复！");
                return;
            }
            
            String reply = JOptionPane.showInputDialog(this, "请输入回复内容：");
            if (reply != null && !reply.trim().isEmpty()) {
                ComplaintDAO complaintDAO = new ComplaintDAO();
                List<Complaint> complaints = complaintDAO.getAllComplaints();
                
                for (Complaint complaint : complaints) {
                    if (complaint.getComplaintId().equals(complaintId)) {
                        complaint.setStatus("已回复");
                        complaint.setReply(reply);
                        complaintDAO.updateComplaint(complaint);
                        break;
                    }
                }
                
                loadComplaintData(model);
                JOptionPane.showMessageDialog(this, "回复成功！");
            }
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadComplaintData(DefaultTableModel model) {
        ComplaintDAO complaintDAO = new ComplaintDAO();
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        
        model.setRowCount(0);
        for (Complaint complaint : complaints) {
            model.addRow(new Object[]{
                complaint.getComplaintId(),
                complaint.getUsername(),
                complaint.getType(),
                complaint.getTitle(),
                complaint.getContent(),
                complaint.getContact(),
                complaint.getStatus(),
                complaint.getReply(),
                complaint.getCreateTime()
            });
        }
    }
    
    // 刷新方法
    private void refreshUserData() {
        if (userTable != null) {
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            refreshUserData(model);
        }
    }
    
    private void refreshUserData(DefaultTableModel model) {
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();
            
            model.setRowCount(0);
            for (User user : users) {
                model.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getResidentId() == null || user.getResidentId().isEmpty() ? 
                        "未绑定" : user.getResidentId(),
                    user.getCreateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新用户数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshRepairData() {
        DefaultTableModel model = (DefaultTableModel) repairTable.getModel();
        loadRepairData(model);
    }
    
    private void refreshComplaintData() {
        DefaultTableModel model = (DefaultTableModel) complaintTable.getModel();
        loadComplaintData(model);
    }
    
    private void createTopPanel(JPanel mainPanel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // 左侧欢迎信息
        JLabel welcomeLabel = new JLabel("欢迎您，管理员 " + currentUser.getUsername());
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        
        // 右侧按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // 刷新按钮
        JButton refreshButton = new JButton("刷新数据");
        StyleUtil.setButtonStyle(refreshButton, StyleUtil.PRIMARY_COLOR);
        refreshButton.addActionListener(e -> refreshAllData());
        
        // 退出按钮
        JButton logoutButton = new JButton("退出登录");
        StyleUtil.setButtonStyle(logoutButton, StyleUtil.DANGER_COLOR);
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }
    
    // 添加这些方法到类中
    private void refreshAllData() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        switch (selectedIndex) {
            case 0: // 用户管理
                refreshUserData();
                break;
            case 1: // 报修管理
                refreshRepairData();
                break;
            case 2: // 投诉管理
                refreshComplaintData();
                break;
            case 3: // 房型管理
                refreshHouseTypeData();
                break;
        }
        JOptionPane.showMessageDialog(this, "数据已刷新！");
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "确定要退出登录吗？",
            "认退出",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    private void loadUserData(DefaultTableModel model) {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        model.setRowCount(0);
        for (User user : users) {
            model.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.getStatus(),
                user.getCreateTime()
            });
        }
    }
    
    private void loadRepairData(DefaultTableModel model) {
        RepairDAO repairDAO = new RepairDAO();
        List<Repair> repairs = repairDAO.getAllRepairs();
        
        model.setRowCount(0);
        for (Repair repair : repairs) {
            model.addRow(new Object[]{
                repair.getRepairId(),
                repair.getUsername(),
                repair.getType(),
                repair.getDescription(),
                repair.getContact(),
                repair.getStatus(),
                repair.getCreateTime()
            });
        }
    }
    
    private JPanel createHouseTypeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加房型");
        JButton editButton = new JButton("编辑房型");
        JButton deleteButton = new JButton("删除房型");
        JButton saveButton = new JButton("保存修改");
        
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        StyleUtil.setButtonStyle(editButton, StyleUtil.PRIMARY_COLOR);
        StyleUtil.setButtonStyle(deleteButton, StyleUtil.DANGER_COLOR);
        StyleUtil.setButtonStyle(saveButton, StyleUtil.WARNING_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(saveButton);
        
        // 表格
        String[] columns = {"房型编号", "房型名称", "建筑面积", "描述", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Double.class;
                }
                return String.class;
            }
        };
        
        houseTypeTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(houseTypeTable);
        
        // 加载初始数据
        refreshHouseTypeData(model);
        
        // 添加按钮事件处理
        addButton.addActionListener(e -> {
            try {
                AddHouseTypeDialog dialog = new AddHouseTypeDialog(this);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshHouseTypeData(model);
                    JOptionPane.showMessageDialog(this, "房型添加成功！");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "添加房型时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 编辑按钮件处理
        editButton.addActionListener(e -> {
            int selectedRow = houseTypeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要编辑的房型！");
                return;
            }
            
            String typeId = (String) houseTypeTable.getValueAt(selectedRow, 0);
            try {
                EditHouseTypeDialog dialog = new EditHouseTypeDialog(this, typeId);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshHouseTypeData(model);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "编辑房型时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 删除按钮事件处理
        deleteButton.addActionListener(e -> {
            int selectedRow = houseTypeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的房型！");
                return;
            }
            
            int result = JOptionPane.showConfirmDialog(this, 
                "确定要删除该房型吗？", "确认删除", 
                JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                try {
                    String typeId = (String) houseTypeTable.getValueAt(selectedRow, 0);
                    HouseTypeDAO houseTypeDAO = new HouseTypeDAO();
                    houseTypeDAO.deleteHouseType(typeId);
                    refreshHouseTypeData(model);
                    JOptionPane.showMessageDialog(this, "房型删除成功！");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "删除房型时发生错误：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 保存按钮事件处理
        saveButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "确定要保存修改吗？", "确认保存", 
                JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                try {
                    saveHouseTypeChanges(model);
                    JOptionPane.showMessageDialog(this, "保存成功！");
                    refreshHouseTypeData(model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "保存修改时发生错误：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void saveHouseTypeChanges(DefaultTableModel model) {
        HouseTypeDAO houseTypeDAO = new HouseTypeDAO();
        int rowCount = model.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            String typeId = (String) model.getValueAt(i, 0);
            String name = (String) model.getValueAt(i, 1);
            double area = (Double) model.getValueAt(i, 2);
            String description = (String) model.getValueAt(i, 3);
            String createTime = (String) model.getValueAt(i, 4);
            
            HouseType houseType = new HouseType(
                typeId, name, area, description, createTime
            );
            
            houseTypeDAO.updateHouseType(houseType);
        }
    }
    
    // 修改无参数的刷新方法
    private void refreshHouseTypeData() {
        if (houseTypeTable != null) {
            DefaultTableModel model = (DefaultTableModel) houseTypeTable.getModel();
            refreshHouseTypeData(model);
        }
    }
    
    // 保持带参数的刷新方法不变
    private void refreshHouseTypeData(DefaultTableModel model) {
        try {
            HouseTypeDAO houseTypeDAO = new HouseTypeDAO();
            List<HouseType> houseTypes = houseTypeDAO.getAllHouseTypes();
            
            model.setRowCount(0);
            for (HouseType houseType : houseTypes) {
                model.addRow(new Object[]{
                    houseType.getTypeId(),
                    houseType.getTypeName(),
                    houseType.getArea(),
                    houseType.getDescription(),
                    houseType.getCreateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新房型数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createResidentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加住户");
        JButton editButton = new JButton("编辑住户");
        JButton deleteButton = new JButton("删除住户");
        JButton saveButton = new JButton("保存修改");
        JButton sortByArrearsButton = new JButton("按欠费金额排序");
        
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        StyleUtil.setButtonStyle(editButton, StyleUtil.PRIMARY_COLOR);
        StyleUtil.setButtonStyle(deleteButton, StyleUtil.DANGER_COLOR);
        StyleUtil.setButtonStyle(saveButton, StyleUtil.WARNING_COLOR);
        StyleUtil.setButtonStyle(sortByArrearsButton, StyleUtil.INFO_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(saveButton);
        toolBar.add(sortByArrearsButton);
        
        // 表格
        String[] columns = {"住户编号", "姓名", "电话", "地址", "预付金额", "欠费金额", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 6;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5) {
                    return Double.class;
                }
                return String.class;
            }
        };
        
        residentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(residentTable);
        
        // 加载数据
        refreshResidentData(model);
        
        // 添加按钮事件处理
        addButton.addActionListener(e -> {
            try {
                AddResidentDialog dialog = new AddResidentDialog(this);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshResidentData(model);
                    JOptionPane.showMessageDialog(this, "住户添加成功！");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "添加住户时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 编辑按钮事件处理
        editButton.addActionListener(e -> {
            int selectedRow = residentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要编辑的住户！");
                return;
            }
            
            String residentId = (String) residentTable.getValueAt(selectedRow, 0);
            try {
                EditResidentDialog dialog = new EditResidentDialog(this, residentId);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshResidentData(model);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "编辑住户时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 删除按钮事件理
        deleteButton.addActionListener(e -> {
            int selectedRow = residentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的住户！");
                return;
            }
            
            int result = JOptionPane.showConfirmDialog(this, 
                "确定要删除该住户吗？", "确认删除", 
                JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                try {
                    String residentId = (String) residentTable.getValueAt(selectedRow, 0);
                    ResidentDAO residentDAO = new ResidentDAO();
                    residentDAO.deleteResident(residentId);
                    refreshResidentData(model);
                    JOptionPane.showMessageDialog(this, "住户删除成功！");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "删除住户时发生错误：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 保存按钮事件处理
        saveButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "确定要保存修改吗？", "确认保存", 
                JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                try {
                    saveResidentChanges(model);
                    JOptionPane.showMessageDialog(this, "保存成功！");
                    refreshResidentData(model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "保存修改时发生错误：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 添加排序按钮事件
        sortByArrearsButton.addActionListener(e -> {
            showArrearsRankingDialog();
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void saveResidentChanges(DefaultTableModel model) {
        ResidentDAO residentDAO = new ResidentDAO();
        int rowCount = model.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            String residentId = (String) model.getValueAt(i, 0);
            String name = (String) model.getValueAt(i, 1);
            String phone = (String) model.getValueAt(i, 2);
            String address = (String) model.getValueAt(i, 3);
            double prepayAmount = (Double) model.getValueAt(i, 4);
            double arrearsAmount = (Double) model.getValueAt(i, 5);
            String createTime = (String) model.getValueAt(i, 6);
            
            Resident resident = new Resident(
                residentId, name, phone, address, prepayAmount, arrearsAmount, createTime
            );
            
            residentDAO.updateResident(resident);
        }
    }
    
    // 修改参数的刷新方法
    private void refreshResidentData() {
        if (residentTable != null) {
            DefaultTableModel model = (DefaultTableModel) residentTable.getModel();
            refreshResidentData(model);
        }
    }
    
    // 保持带参数的刷新方法不变
    private void refreshResidentData(DefaultTableModel model) {
        try {
            ResidentDAO residentDAO = new ResidentDAO();
            List<Resident> residents = residentDAO.getAllResidents();
            
            model.setRowCount(0);
            for (Resident resident : residents) {
                model.addRow(new Object[]{
                    resident.getResidentId(),
                    resident.getName(),
                    resident.getPhone(),
                    resident.getAddress(),
                    resident.getPrepayAmount(),
                    resident.getArrearsAmount(),
                    resident.getCreateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新住户数据发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 获取用户绑定的住户ID
    private String getUserResidentId(int selectedRow) {
        String userId = (String) userTable.getValueAt(selectedRow, 0);
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);
        return user != null ? user.getResidentId() : null;
    }
    
    // 显示欠费排名对话框
    private void showArrearsRankingDialog() {
        JDialog dialog = new JDialog(this, "住户欠费金额排名", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        // 创建表格模型
        String[] columns = {"排名", "住户编号", "姓名", "联系电话", "欠费金额"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 4) { // 欠费额列
                    return Double.class;
                }
                return String.class;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // 获取并排序住户数据
        ResidentDAO residentDAO = new ResidentDAO();
        List<Resident> residents = residentDAO.getAllResidents();
        
        // 按欠费金额排序
        residents.sort((r1, r2) -> 
            Double.compare(r2.getArrearsAmount(), r1.getArrearsAmount()));
        
        // 填充表格数据
        int rank = 1;
        for (Resident resident : residents) {
            if (resident.getArrearsAmount() > 0) {  // 只显示有欠费的住户
                model.addRow(new Object[]{
                    rank++,
                    resident.getResidentId(),
                    resident.getName(),
                    resident.getPhone(),
                    resident.getArrearsAmount()
                });
            }
        }
        
        // 设置表格样式
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        
        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // 排名
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // 住户编号
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // 姓名
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // 联系电话
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // 欠费金额
        
        // 添加关闭按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("关闭");
        StyleUtil.setButtonStyle(closeButton, StyleUtil.PRIMARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        // 设置对话框布局
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 显示对话框
        dialog.setVisible(true);
    }
    
    private JPanel createParkingManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加车位");
        JButton editButton = new JButton("编辑车位");
        JButton deleteButton = new JButton("删除车位");
        JButton bindButton = new JButton("绑定住户");
        JButton viewButton = new JButton("查看住户车位");
        
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        StyleUtil.setButtonStyle(editButton, StyleUtil.PRIMARY_COLOR);
        StyleUtil.setButtonStyle(deleteButton, StyleUtil.DANGER_COLOR);
        StyleUtil.setButtonStyle(bindButton, StyleUtil.WARNING_COLOR);
        StyleUtil.setButtonStyle(viewButton, StyleUtil.INFO_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(bindButton);
        toolBar.add(viewButton);
        
        // 表格
        String[] columns = {"车位编号", "位置", "状态", "绑定住户", "车牌号", "月租费用", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable parkingTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(parkingTable);
        
        // 添加按钮事件
        addButton.addActionListener(e -> {
            ParkingSpaceDialog dialog = new ParkingSpaceDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                refreshParkingData(model);
            }
        });
        
        // 编辑按钮事件
        editButton.addActionListener(e -> {
            int selectedRow = parkingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要编辑的车位！");
                return;
            }
            
            String spaceId = (String) parkingTable.getValueAt(selectedRow, 0);
            ParkingSpaceDAO dao = new ParkingSpaceDAO();
            ParkingSpace space = dao.getParkingSpaceById(spaceId);
            
            if (space != null) {
                ParkingSpaceDialog dialog = new ParkingSpaceDialog(this, space);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    refreshParkingData(model);
                }
            }
        });
        
        // 删除按钮事件
        deleteButton.addActionListener(e -> {
            int selectedRow = parkingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的车位！");
                return;
            }
            
            int choice = JOptionPane.showConfirmDialog(this,
                "确定要删除选中的车位吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);
                
            if (choice == JOptionPane.YES_OPTION) {
                String spaceId = (String) parkingTable.getValueAt(selectedRow, 0);
                ParkingSpaceDAO dao = new ParkingSpaceDAO();
                dao.deleteParkingSpace(spaceId);
                refreshParkingData(model);
            }
        });
        
        // 绑定住户按钮事
        bindButton.addActionListener(e -> {
            int selectedRow = parkingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要绑定的车位！");
                return;
            }
            
            String spaceId = (String) parkingTable.getValueAt(selectedRow, 0);
            BindParkingResidentDialog dialog = new BindParkingResidentDialog(this, spaceId);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                refreshParkingData(model);
            }
        });
        
        // 查看住户车位按钮事件
        viewButton.addActionListener(e -> {
            ViewResidentParkingDialog dialog = new ViewResidentParkingDialog(this);
            dialog.setVisible(true);
        });
        
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 初始加载数据
        refreshParkingData(model);
        
        return panel;
    }
    
    private void refreshParkingData(DefaultTableModel model) {
        try {
            ParkingSpaceDAO dao = new ParkingSpaceDAO();
            List<ParkingSpace> spaces = dao.getAllParkingSpaces();
            
            model.setRowCount(0);
            for (ParkingSpace space : spaces) {
                String residentInfo = "";
                if (space.getResidentId() != null && !space.getResidentId().isEmpty()) {
                    ResidentDAO residentDAO = new ResidentDAO();
                    Resident resident = residentDAO.getResidentById(space.getResidentId());
                    if (resident != null) {
                        residentInfo = resident.getName() + " (" + space.getResidentId() + ")";
                    }
                }
                
                model.addRow(new Object[]{
                    space.getSpaceId(),
                    space.getLocation(),
                    space.getStatus(),
                    residentInfo,
                    space.getCarNumber(),
                    space.getMonthlyFee(),
                    space.getCreateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新车位数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createCommunityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 小区名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("小区名称："), gbc);
        
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        // 楼宇总数
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("楼宇总数："), gbc);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 999, 1);
        JSpinner buildingCountSpinner = new JSpinner(spinnerModel);
        gbc.gridx = 1;
        formPanel.add(buildingCountSpinner, gbc);
        
        // 保存按钮
        JButton saveButton = new JButton("保存修改");
        StyleUtil.setButtonStyle(saveButton, StyleUtil.PRIMARY_COLOR);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);
        
        // 加载现有数据
        CommunityDAO communityDAO = new CommunityDAO();
        List<Community> communities = communityDAO.getAllCommunities();
        if (!communities.isEmpty()) {
            Community community = communities.get(0);
            nameField.setText(community.getName());
            buildingCountSpinner.setValue(community.getBuildingCount());
        }
        
        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "小区名称不能为空！");
                    return;
                }
                
                int buildingCount = (Integer) buildingCountSpinner.getValue();
                
                Community community;
                if (communities.isEmpty()) {
                    community = new Community(
                        "C001",  // 新建小区ID
                        name,
                        buildingCount,
                        0,  // 单元数默认为0
                        "",  // 地址默认为空
                        "",  // 描述默认为空
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    );
                    communityDAO.addCommunity(community);
                } else {
                    community = communities.get(0);
                    community.setName(name);
                    community.setBuildingCount(buildingCount);
                    communityDAO.updateCommunity(community);
                }
                
                JOptionPane.showMessageDialog(this, "小区信息保存成功！");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "保存小区信息时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 将表单面板添加到面板
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createFeeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 收费名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("收费名称："), gbc);
        
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        // 收费金额
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("收费金额："), gbc);
        
        JTextField amountField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);
        
        // 收费单位
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("收费单位："), gbc);
        
        String[] units = {"元/月", "元/年", "元/次", "元/平方米"};
        JComboBox<String> unitCombo = new JComboBox<>(units);
        gbc.gridx = 1;
        formPanel.add(unitCombo, gbc);
        
        // 收费周期
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("收费周期："), gbc);
        
        String[] cycles = {"月付", "季付", "年付", "一次性"};
        JComboBox<String> cycleCombo = new JComboBox<>(cycles);
        gbc.gridx = 1;
        formPanel.add(cycleCombo, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        // 添加按钮
        JButton addButton = new JButton("添加收费项目");
        StyleUtil.setButtonStyle(addButton, StyleUtil.SUCCESS_COLOR);
        
        // 刷新按钮
        JButton refreshButton = new JButton("刷新数据");
        StyleUtil.setButtonStyle(refreshButton, StyleUtil.PRIMARY_COLOR);
        
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // 创建表格显示已有收费项目
        String[] columns = {"收费编号", "收费名称", "收费金额", "收费单位", "收费周期", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable feeTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(feeTable);
        
        // 设置表格样式
        feeTable.setRowHeight(25);
        feeTable.getTableHeader().setReorderingAllowed(false);
        
        // 添加按钮事件
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String amountText = amountField.getText().trim();
                
                if (name.isEmpty() || amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请填写完整信息！");
                    return;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(amountText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入有效的金额！");
                    return;
                }
                
                FeeItemDAO dao = new FeeItemDAO();
                String itemId = dao.generateNewItemId();
                String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                FeeItem item = new FeeItem(
                    itemId,
                    name,
                    amount,
                    (String) unitCombo.getSelectedItem(),
                    (String) cycleCombo.getSelectedItem(),
                    currentTime
                );
                
                dao.addFeeItem(item);
                
                // 刷新表格数据
                refreshFeeItemData(model);
                
                // 清空输入框
                nameField.setText("");
                amountField.setText("");
                unitCombo.setSelectedIndex(0);
                cycleCombo.setSelectedIndex(0);
                
                JOptionPane.showMessageDialog(this, "收费项目添加成功！");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "添加收费项目时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 刷新按钮事件
        refreshButton.addActionListener(e -> {
            try {
                refreshFeeItemData(model);
                JOptionPane.showMessageDialog(this, "数据刷新成功！");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "刷新数据时发生错误：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 初始加载数据
        refreshFeeItemData(model);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshFeeItemData(DefaultTableModel model) {
        try {
            FeeItemDAO dao = new FeeItemDAO();
            List<FeeItem> items = dao.getAllFeeItems();
            
            model.setRowCount(0);  // 清空表格数据
            
            for (FeeItem item : items) {
                model.addRow(new Object[]{
                    item.getItemId(),
                    item.getName(),
                    item.getAmount(),
                    item.getUnit(),
                    item.getCycle(),
                    item.getCreateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "刷新收费项目数据时发生错误：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 