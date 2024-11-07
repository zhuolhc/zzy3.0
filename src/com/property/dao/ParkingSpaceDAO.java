package com.property.dao;

import com.property.model.ParkingSpace;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSpaceDAO {
    private static final String FILE_NAME = "data/parking_spaces.txt";
    
    public void addParkingSpace(ParkingSpace space) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(space));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateParkingSpace(ParkingSpace space) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            ParkingSpace existingSpace = parseSpace(line);
            if (existingSpace != null && existingSpace.getSpaceId().equals(space.getSpaceId())) {
                newLines.add(convertToString(space));
                found = true;
            } else {
                newLines.add(line);
            }
        }
        
        if (!found) {
            newLines.add(convertToString(space));
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public void deleteParkingSpace(String spaceId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            ParkingSpace space = parseSpace(line);
            if (space != null && !space.getSpaceId().equals(spaceId)) {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public ParkingSpace getParkingSpaceById(String spaceId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        
        for (String line : lines) {
            ParkingSpace space = parseSpace(line);
            if (space != null && space.getSpaceId().equals(spaceId)) {
                return space;
            }
        }
        
        return null;
    }
    
    public List<ParkingSpace> getAllParkingSpaces() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<ParkingSpace> spaces = new ArrayList<>();
        
        for (String line : lines) {
            ParkingSpace space = parseSpace(line);
            if (space != null) {
                spaces.add(space);
            }
        }
        
        return spaces;
    }
    
    public List<ParkingSpace> getParkingSpacesByResidentId(String residentId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<ParkingSpace> spaces = new ArrayList<>();
        
        for (String line : lines) {
            ParkingSpace space = parseSpace(line);
            if (space != null && residentId.equals(space.getResidentId())) {
                spaces.add(space);
            }
        }
        
        return spaces;
    }
    
    public String generateNewSpaceId() {
        List<ParkingSpace> spaces = getAllParkingSpaces();
        int maxId = 0;
        
        for (ParkingSpace space : spaces) {
            try {
                int id = Integer.parseInt(space.getSpaceId().substring(1));
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException e) {
                // 忽略格式错误的ID
            }
        }
        
        return "P" + String.format("%03d", maxId + 1);
    }
    
    private ParkingSpace parseSpace(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                return new ParkingSpace(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    Double.parseDouble(parts[5].trim()),
                    parts[6].trim()
                );
            }
        } catch (Exception e) {
            System.err.println("解析车位数据出错: " + line);
        }
        return null;
    }
    
    private String convertToString(ParkingSpace space) {
        return String.join(",",
            space.getSpaceId(),
            space.getLocation(),
            space.getStatus(),
            space.getResidentId() == null ? "" : space.getResidentId(),
            space.getCarNumber() == null ? "" : space.getCarNumber(),
            String.valueOf(space.getMonthlyFee()),
            space.getCreateTime()
        );
    }
} 