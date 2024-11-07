import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFrame() {
        setTitle("小区物业管理系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        
        // 添加组件
        panel.add(new JLabel("用户名："));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("密码："));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        loginButton = new JButton("登录");
        panel.add(loginButton);
        
        // 添加登录按钮事件
        loginButton.addActionListener(e -> login());
        
        add(panel);
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // 验证登录信息
        UserController userController = new UserController();
        User user = userController.login(username, password);
        
        if (user != null) {
            if (user.getRole().equals("ADMIN")) {
                new AdminMainFrame(user).setVisible(true);
            } else {
                new UserMainFrame(user).setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！");
        }
    }
} 