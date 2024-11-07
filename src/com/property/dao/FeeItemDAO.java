package com.property.dao;

import com.property.model.FeeItem;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class FeeItemDAO {
    private static final String FILE_NAME = "data/fee_items.txt";
    
    public void addFeeItem(FeeItem item) {
        try {
            List<String> lines = FileUtil.readFromFile(FILE_NAME);
            String newLine = convertToString(item);
            System.out.println("Adding new fee item: " + newLine);
            lines.add(newLine);
            FileUtil.writeToFile(FILE_NAME, lines);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("添加收费项目失败: " + e.getMessage());
        }
    }
    
    public List<FeeItem> getAllFeeItems() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<FeeItem> items = new ArrayList<>();
        
        for (String line : lines) {
            FeeItem item = parseFeeItem(line);
            if (item != null) {
                items.add(item);
            }
        }
        
        return items;
    }
    
    private FeeItem parseFeeItem(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                return new FeeItem(
                    parts[0].trim(),
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String convertToString(FeeItem item) {
        return String.join(",",
            item.getItemId(),
            item.getName(),
            String.valueOf(item.getAmount()),
            item.getUnit(),
            item.getCycle(),
            item.getCreateTime()
        );
    }
    
    public String generateNewItemId() {
        List<FeeItem> items = getAllFeeItems();
        int maxId = 0;
        for (FeeItem item : items) {
            try {
                int id = Integer.parseInt(item.getItemId().substring(1));
                maxId = Math.max(maxId, id);
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        return "F" + String.format("%03d", maxId + 1);
    }
} 