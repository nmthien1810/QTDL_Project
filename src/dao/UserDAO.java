package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserDAO {

    // Hàm kiểm tra đăng nhập, trả về đối tượng User nếu thành công, trả về null nếu
    // thất bại
    public User login(String username, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Truyền tham số vào dấu ? trong câu SQL
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // Nếu có kết quả trả về, nghĩa là đăng nhập đúng
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAdmin(rs.getBoolean("is_admin"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return user;

    }
}