package com.property.model;

public class Notification {
    private String notificationId;
    private String userId;
    private String title;
    private String content;
    private boolean isRead;
    private String createTime;
    
    public Notification(String notificationId, String userId, String title, 
                       String content, boolean isRead, String createTime) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.createTime = createTime;
    }
    
    // Getter方法
    public String getNotificationId() { return notificationId; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public boolean isRead() { return isRead; }
    public String getCreateTime() { return createTime; }
    
    // Setter方法
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setRead(boolean read) { isRead = read; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
} 