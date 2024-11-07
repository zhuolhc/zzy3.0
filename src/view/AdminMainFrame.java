import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    
    public AdminMainFrame(User admin) {
        setTitle("管理员界面 - " + admin.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("小区资料管理", createCommunityPanel());
        tabbedPane.addTab("房型资料管理", createHouseTypePanel());
        tabbedPane.addTab("住户管理", createResidentPanel());
        tabbedPane.addTab("车位管理", createParkingPanel());
        tabbedPane.addTab("物业收费管理", createPropertyFeePanel());
        
        add(tabbedPane);
    }
    
    private JPanel createCommunityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // 添加小区资料管理组件
        return panel;
    }
    
    private JPanel createHouseTypePanel() {
        return new JPanel();
    }
    
    private JPanel createResidentPanel() {
        return new JPanel();
    }
    
    private JPanel createParkingPanel() {
        return new JPanel();
    }
    
    private JPanel createPropertyFeePanel() {
        return new JPanel();
    }
} 