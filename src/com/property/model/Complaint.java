package com.property.model;

public class Complaint {
    private String complaintId;
    private String userId;
    private String username;
    private String type;
    private String title;
    private String content;
    private String contact;
    private String status;
    private String reply;
    private String createTime;
    
    public Complaint(String complaintId, String userId, String username, 
                    String type, String title, String content, String contact, 
                    String status, String reply, String createTime) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.status = status;
        this.reply = reply;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getComplaintId() { return complaintId; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }
    public String getReply() { return reply; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setType(String type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setContact(String contact) { this.contact = contact; }
    public void setStatus(String status) { this.status = status; }
    public void setReply(String reply) { this.reply = reply; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 