import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // 初始化数据文件
        initializeDataFiles();
        
        // 启动登录界面
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
    
    private static void initializeDataFiles() {
        // 确保data目录存在
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
        // 初始化默认管理员账号
        if (!new File("data/users.txt").exists()) {
            List<String> defaultUsers = new ArrayList<>();
            // 格式：用户ID,用户名,密码,角色
            defaultUsers.add("1,admin,admin123,ADMIN");
            FileUtil.writeToFile("users.txt", defaultUsers);
        }
        
        // 初始化其他必要的数据文件
        String[] files = {
            "community.txt",
            "house_types.txt",
            "residents.txt",
            "repairs.txt",
            "complaints.txt",
            "parking_spaces.txt",
            "property_fees.txt"
        };
        
        for (String file : files) {
            File f = new File("data/" + file);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
} 