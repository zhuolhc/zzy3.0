package com.property.dao;

import com.property.model.Resident;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class ResidentDAO {
    private static final String FILE_NAME = "data/residents.txt";
    
    public void addResident(Resident resident) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(resident));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateResident(Resident resident) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Resident existingResident = parseResident(line);
            if (existingResident != null && 
                existingResident.getResidentId().equals(resident.getResidentId())) {
                newLines.add(convertToString(resident));
            } else {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public void deleteResident(String residentId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            Resident resident = parseResident(line);
            if (resident != null && !resident.getResidentId().equals(residentId)) {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public List<Resident> getAllResidents() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Resident> residents = new ArrayList<>();
        
        for (String line : lines) {
            Resident resident = parseResident(line);
            if (resident != null) {
                residents.add(resident);
            }
        }
        
        return residents;
    }
    
    public Resident getResidentById(String residentId) {
        List<Resident> residents = getAllResidents();
        for (Resident resident : residents) {
            if (resident.getResidentId().equals(residentId)) {
                return resident;
            }
        }
        return null;
    }
    
    private Resident parseResident(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                return new Resident(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    Double.parseDouble(parts[4].trim()),
                    Double.parseDouble(parts[5].trim()),
                    parts[6].trim()
                );
            }
        } catch (NumberFormatException e) {
            System.err.println("解析住户数据出错: " + line);
        }
        return null;
    }
    
    private String convertToString(Resident resident) {
        return String.join(",",
            resident.getResidentId(),
            resident.getName(),
            resident.getPhone(),
            resident.getAddress(),
            String.valueOf(resident.getPrepayAmount()),
            String.valueOf(resident.getArrearsAmount()),
            resident.getCreateTime()
        );
    }
    
    public String generateNewResidentId() {
        List<Resident> residents = getAllResidents();
        int maxId = 0;
        
        for (Resident resident : residents) {
            try {
                int id = Integer.parseInt(resident.getResidentId().substring(1));
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException e) {
                // 忽略格式错误的ID
            }
        }
        
        return "R" + String.format("%03d", maxId + 1);
    }
} 