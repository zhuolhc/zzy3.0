package com.property.model;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role;
    private String residentId;
    private String status;
    private String createTime;
    
    public User() {
        // 默认构造函数
    }
    
    // 添加一个不带residentId的构造函数
    public User(String userId, String username, String password, 
                String role, String status, String createTime) {
        this(userId, username, password, role, "", status, createTime);
    }
    
    // 完整的构造函数
    public User(String userId, String username, String password, 
                String role, String residentId, String status, String createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.residentId = residentId;
        this.status = status;
        this.createTime = createTime;
    }
    
    // Getter和Setter方法
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 