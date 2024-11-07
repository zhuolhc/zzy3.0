package com.property.dao;

import com.property.model.Notification;
import com.property.util.FileUtil;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationDAO {
    private static final String FILE_NAME = "data/notifications.txt";
    
    public void addNotification(Notification notification) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        lines.add(convertToString(notification));
        FileUtil.writeToFile(FILE_NAME, lines);
    }
    
    public List<Notification> getUnreadNotifications(String userId) {
        List<String> lines = FileUtil.readFromFile(FILE_NAME);
        List<Notification> notifications = new ArrayList<>();
        
        for (String line : lines) {
            Notification notification = parseNotification(line);
            if (notification != null && 
                notification.getUserId().equals(userId) && 
                !notification.isRead()) {
                notifications.add(notification);
            }
        }
        
        return notifications;
    }
    
    private Notification parseNotification(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 6) {
            return new Notification(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                Boolean.parseBoolean(parts[4].trim()),
                parts[5].trim()
            );
        }
        return null;
    }
    
    private String convertToString(Notification notification) {
        return String.join(",",
            notification.getNotificationId(),
            notification.getUserId(),
            notification.getTitle(),
            notification.getContent(),
            String.valueOf(notification.isRead()),
            notification.getCreateTime()
        );
    }
} 