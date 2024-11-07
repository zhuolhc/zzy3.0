package com.property.dao;

import com.property.model.User;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {
    private static final String FILE_NAME = "data/users.txt";
    
    public List<User> getAllUsers() {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                users.add(parseUser(line));
            }
        }
        return users;
    }
    
    private User parseUser(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                return new User(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim()
                );
            } else if (parts.length >= 6) {
                return new User(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim()
                );
            }
        } catch (Exception e) {
            System.err.println("解析用户数据出错: " + line);
        }
        return null;
    }
    
    public void addUser(User user) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(user));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public void updateUser(User user) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            User existingUser = parseUser(line);
            if (existingUser != null && existingUser.getUserId().equals(user.getUserId())) {
                newLines.add(convertToString(user));
                found = true;
            } else {
                newLines.add(line);
            }
        }
        
        if (!found) {
            newLines.add(convertToString(user));
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public void deleteUser(String userId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        
        for (String line : lines) {
            User user = parseUser(line);
            if (user != null && !user.getUserId().equals(userId)) {
                newLines.add(line);
            }
        }
        
        FileUtil.writeToFile(FILE_NAME, newLines);
    }
    
    public User getUserById(String userId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        
        for (String line : lines) {
            User user = parseUser(line);
            if (user != null && user.getUserId().equals(userId)) {
                return user;
            }
        }
        
        return null;
    }
    
    public User getUserByUsername(String username) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        
        for (String line : lines) {
            User user = parseUser(line);
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        
        // 如果没有找到用户，检查是否是默认管理员
        if ("admin".equals(username)) {
            return new User(
                "1",
                "admin",
                "admin123",
                "ADMIN",
                "",
                "正常",
                "2024-03-21 00:00:00"
            );
        }
        
        return null;
    }
    
    public boolean isUsernameExists(String username, String excludeUserId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        
        for (String line : lines) {
            User user = parseUser(line);
            if (user != null && 
                user.getUsername().equals(username) && 
                !user.getUserId().equals(excludeUserId)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isUsernameExists(String username) {
        return isUsernameExists(username, null);
    }
    
    private String convertToString(User user) {
        return String.join(",",
            user.getUserId(),
            user.getUsername(),
            user.getPassword(),
            user.getRole(),
            user.getResidentId() == null ? "" : user.getResidentId(),
            user.getStatus() == null ? "正常" : user.getStatus(),
            user.getCreateTime()
        );
    }
} 