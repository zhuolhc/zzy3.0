package com.property.util;

import javax.swing.*;
import java.awt.*;

public class StyleUtil {
    public static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    public static final Color SUCCESS_COLOR = new Color(92, 184, 92);
    public static final Color INFO_COLOR = new Color(91, 192, 222);
    public static final Color WARNING_COLOR = new Color(240, 173, 78);
    public static final Color DANGER_COLOR = new Color(217, 83, 79);
    
    public static void setButtonStyle(JButton button, Color color) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
} 