public class UserController {
    public User login(String username, String password) {
        // 这里应该连接数据库进行验证
        // 示例代码仅作演示
        if (username.equals("admin") && password.equals("admin123")) {
            return new User("1", username, password, "ADMIN");
        } else if (username.equals("user") && password.equals("user123")) {
            return new User("2", username, password, "USER");
        }
        return null;
    }
} 