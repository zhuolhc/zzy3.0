package com.property.dao;

import com.property.model.Complaint;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class ComplaintDAO {
    private static final String FILE_NAME = "data/complaints.txt";
    
    public List<Complaint> getAllComplaints() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Complaint> complaints = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                complaints.add(parseComplaint(line));
            }
        }
        return complaints;
    }
    
    public void addComplaint(Complaint complaint) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(complaint));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateComplaint(Complaint complaint) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Complaint existingComplaint = parseComplaint(line);
            if (existingComplaint != null && 
                existingComplaint.getComplaintId().equals(complaint.getComplaintId())) {
                newLines.add(convertToString(complaint));
            } else {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    private Complaint parseComplaint(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 10) {
            return new Complaint(
                parts[0].trim(), // complaintId
                parts[1].trim(), // userId
                parts[2].trim(), // username
                parts[3].trim(), // type
                parts[4].trim(), // title
                parts[5].trim(), // content
                parts[6].trim(), // contact
                parts[7].trim(), // status
                parts[8].trim(), // reply
                parts[9].trim()  // createTime
            );
        }
        return null;
    }
    
    private String convertToString(Complaint complaint) {
        return String.join(",",
            complaint.getComplaintId(),
            complaint.getUserId(),
            complaint.getUsername(),
            complaint.getType(),
            complaint.getTitle(),
            complaint.getContent(),
            complaint.getContact(),
            complaint.getStatus(),
            complaint.getReply(),
            complaint.getCreateTime()
        );
    }
    
    public List<Complaint> getComplaintsByUserId(String userId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Complaint> complaints = new ArrayList<>();
        
        for (String line : lines) {
            Complaint complaint = parseComplaint(line);
            if (complaint != null && complaint.getUserId().equals(userId)) {
                complaints.add(complaint);
            }
        }
        
        return complaints;
    }
    
    public String generateNewComplaintId() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        int maxId = 0;
        
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String complaintId = parts[0].trim();
                    if (complaintId.startsWith("C")) {
                        int id = Integer.parseInt(complaintId.substring(1));
                        maxId = Math.max(maxId, id);
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        
        return "C" + String.format("%03d", maxId + 1);
    }
} 