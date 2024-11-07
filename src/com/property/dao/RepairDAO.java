package com.property.dao;

import com.property.model.Repair;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class RepairDAO {
    private static final String FILE_NAME = "data/repairs.txt";
    
    public List<Repair> getAllRepairs() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Repair> repairs = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                repairs.add(parseRepair(line));
            }
        }
        return repairs;
    }
    
    public void addRepair(Repair repair) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(repair));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateRepair(Repair repair) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Repair existingRepair = parseRepair(line);
            if (existingRepair != null && existingRepair.getRepairId().equals(repair.getRepairId())) {
                newLines.add(convertToString(repair));
            } else {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    private Repair parseRepair(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 8) {
            return new Repair(
                parts[0].trim(), // repairId
                parts[1].trim(), // userId
                parts[2].trim(), // username
                parts[3].trim(), // type
                parts[4].trim(), // description
                parts[5].trim(), // contact
                parts[6].trim(), // status
                parts[7].trim()  // createTime
            );
        }
        return null;
    }
    
    private String convertToString(Repair repair) {
        return String.join(",",
            repair.getRepairId(),
            repair.getUserId(),
            repair.getUsername(),
            repair.getType(),
            repair.getDescription(),
            repair.getContact(),
            repair.getStatus(),
            repair.getCreateTime()
        );
    }
} 