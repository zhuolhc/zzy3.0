package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import com.property.model.Complaint;
import com.property.dao.ComplaintDAO;
import java.util.List;

public class ViewComplaintDialog extends JDialog {
    private Complaint complaint;
    
    public ViewComplaintDialog(JFrame parent, String complaintId) {
        super(parent, "投诉详情", true);
        loadComplaint(complaintId);
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    private void loadComplaint(String complaintId) {
        ComplaintDAO dao = new ComplaintDAO();
        List<Complaint> complaints = dao.getAllComplaints();
        for (Complaint c : complaints) {
            if (c.getComplaintId().equals(complaintId)) {
                this.complaint = c;
                break;
            }
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // 投诉编号
        addField(mainPanel, gbc, row++, "投诉编号：", complaint.getComplaintId());
        
        // 投诉人
        addField(mainPanel, gbc, row++, "投诉人：", complaint.getUsername());
        
        // 投诉类型
        addField(mainPanel, gbc, row++, "投诉类型：", complaint.getType());
        
        // 投诉标题
        addField(mainPanel, gbc, row++, "投诉标题：", complaint.getTitle());
        
        // 投诉内容
        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel("投诉内容："), gbc);
        
        JTextArea contentArea = new JTextArea(complaint.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setRows(4);
        
        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(contentArea), gbc);
        
        // 联系方式
        addField(mainPanel, gbc, row++, "联系方式：", complaint.getContact());
        
        // 提交时间
        addField(mainPanel, gbc, row++, "提交时间：", complaint.getCreateTime());
        
        // 处理状态
        addField(mainPanel, gbc, row++, "处理状态：", complaint.getStatus());
        
        // 如果已处理，显示回复内容
        if (!"待处理".equals(complaint.getStatus())) {
            gbc.gridx = 0;
            gbc.gridy = row++;
            mainPanel.add(new JLabel("回复内容："), gbc);
            
            JTextArea replyArea = new JTextArea(complaint.getReply());
            replyArea.setEditable(false);
            replyArea.setLineWrap(true);
            replyArea.setWrapStyleWord(true);
            replyArea.setRows(3);
            
            gbc.gridx = 1;
            mainPanel.add(new JScrollPane(replyArea), gbc);
        }
        
        // 关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        
        // 添加到主面板
        setLayout(new BorderLayout());
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addField(JPanel panel, GridBagConstraints gbc, int row, 
                         String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }
} 