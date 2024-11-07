package com.property.dao;

import com.property.model.HouseType;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class HouseTypeDAO {
    private static final String FILE_NAME = "data/housetypes.txt";
    
    // 添加房型
    public void addHouseType(HouseType houseType) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(houseType));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    // 更新房型
    public void updateHouseType(HouseType houseType) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            HouseType existingHouseType = parseHouseType(line);
            if (existingHouseType != null && 
                existingHouseType.getTypeId().equals(houseType.getTypeId())) {
                newLines.add(convertToString(houseType));
            } else {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    // 删除房型
    public void deleteHouseType(String typeId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            HouseType houseType = parseHouseType(line);
            if (houseType != null && !houseType.getTypeId().equals(typeId)) {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    // 获取所有房型
    public List<HouseType> getAllHouseTypes() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<HouseType> houseTypes = new ArrayList<>();
        
        for (String line : lines) {
            HouseType houseType = parseHouseType(line);
            if (houseType != null) {
                houseTypes.add(houseType);
            }
        }
        
        return houseTypes;
    }
    
    // 根据ID获取房型
    public HouseType getHouseTypeById(String typeId) {
        List<HouseType> houseTypes = getAllHouseTypes();
        for (HouseType houseType : houseTypes) {
            if (houseType.getTypeId().equals(typeId)) {
                return houseType;
            }
        }
        return null;
    }
    
    // 将字符串解析为房型对象
    private HouseType parseHouseType(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                return new HouseType(
                    parts[0].trim(),                    // typeId
                    parts[1].trim(),                    // typeName
                    Double.parseDouble(parts[2].trim()), // area
                    parts[3].trim(),                    // description
                    parts[4].trim()                     // createTime
                );
            }
        } catch (NumberFormatException e) {
            System.err.println("解析房型数据出错: " + line);
        }
        return null;
    }
    
    // 将房型对象转换为字符串
    private String convertToString(HouseType houseType) {
        return String.join(",",
            houseType.getTypeId(),
            houseType.getTypeName(),
            String.valueOf(houseType.getArea()),
            houseType.getDescription(),
            houseType.getCreateTime()
        );
    }
    
    // 生成新的房型ID
    public String generateNewTypeId() {
        List<HouseType> houseTypes = getAllHouseTypes();
        int maxId = 0;
        
        for (HouseType houseType : houseTypes) {
            try {
                int id = Integer.parseInt(houseType.getTypeId().substring(1));
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException e) {
                // 忽略格式错误的ID
            }
        }
        
        return "T" + String.format("%03d", maxId + 1);
    }
    
    // 检查房型名称是否已存在
    public boolean isTypeNameExists(String typeName, String excludeTypeId) {
        List<HouseType> houseTypes = getAllHouseTypes();
        for (HouseType houseType : houseTypes) {
            if (houseType.getTypeName().equals(typeName) && 
                !houseType.getTypeId().equals(excludeTypeId)) {
                return true;
            }
        }
        return false;
    }
} 