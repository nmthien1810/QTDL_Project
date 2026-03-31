package view;

import java.util.Scanner;
import dao.BookDAO;
import dao.UserDAO;
import model.User;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        BookDAO bookDAO = new BookDAO();
        UserView userView = new UserView();
        

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ NHÀ SÁCH         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.print("  Nhập username: ");
        String username = scanner.nextLine();
        System.out.print("  Nhập password: ");
        String password = scanner.nextLine();

        User loggedInUser = userDAO.login(username, password);

        if (loggedInUser != null) {
            System.out.println("\n  [✓] Đăng nhập thành công! Xin chào, " + loggedInUser.getUsername());

            if (loggedInUser.isAdmin()) {
                System.out.println("  [*] Bạn đang đăng nhập với quyền ADMIN.");
                // TODO: Gọi menu Admin (Thêm/Xóa/Sửa sách) ở đây

            } else {
                userView.displayUserView(bookDAO);
            }
        } else {
            System.out.println("\n  [X] Sai tài khoản hoặc mật khẩu! Vui lòng chạy lại chương trình.");
        }
        scanner.close();
    }
}