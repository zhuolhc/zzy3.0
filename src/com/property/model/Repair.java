package com.property.model;

public class Repair {
    private String repairId;
    private String userId;
    private String username;
    private String type;
    private String description;
    private String contact;
    private String status;
    private String createTime;
    
    public Repair(String repairId, String userId, String username, String type,
                 String description, String contact, String status, String createTime) {
        this.repairId = repairId;
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.description = description;
        this.contact = contact;
        this.status = status;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getRepairId() { return repairId; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setRepairId(String repairId) { this.repairId = repairId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setContact(String contact) { this.contact = contact; }
    public void setStatus(String status) { this.status = status; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 