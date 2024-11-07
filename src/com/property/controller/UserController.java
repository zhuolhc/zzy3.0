package com.property.controller;

import com.property.model.User;
import com.property.dao.UserDAO;
import java.util.List;

public class UserController {
    private UserDAO userDAO;
    
    public UserController() {
        userDAO = new UserDAO();
    }
    
    public User login(String username, String password) {
        // 先检查是否是默认管理员
        if ("admin".equals(username) && "admin123".equals(password)) {
            return new User(
                "1", 
                "admin", 
                "admin123", 
                "管理员", 
                "", 
                "正常",
                "2024-03-21 00:00:00"
            );
        }
        
        // 检查普通用户
        User user = userDAO.getUserByUsername(username);
        
        if (user != null && 
            user.getPassword().equals(password) && 
            "正常".equals(user.getStatus())) {
            return user;
        }
        
        return null;
    }
    
    public User getDefaultAdmin() {
        return new User(
            "1", 
            "admin", 
            "admin123", 
            "管理员", 
            "", 
            "正常",
            "2024-03-21 00:00:00"
        );
    }
    
    public void addUser(User user) {
        userDAO.addUser(user);
    }
    
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
    
    public void deleteUser(String userId) {
        userDAO.deleteUser(userId);
    }
    
    public boolean isUsernameExists(String username) {
        List<User> users = userDAO.getAllUsers();
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }
} 