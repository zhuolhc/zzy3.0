import javax.swing.*;
import java.awt.*;

public class UserMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    
    public UserMainFrame(User user) {
        this.currentUser = user;
        setTitle("用户界面 - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("个人信息", createPersonalInfoPanel());
        tabbedPane.addTab("报修管理", createRepairPanel());
        tabbedPane.addTab("投诉管理", createComplaintPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        // 添加个人信息显示和编辑组件
        return panel;
    }
    
    private JPanel createRepairPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // 添加报修相关组件
        return panel;
    }
    
    private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // 添加投诉相关组件
        return panel;
    }
} 