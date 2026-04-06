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
        AdminView adminView = new AdminView();
        

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ NHÀ SÁCH         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        User loggedInUser = null;
        while (loggedInUser == null) {
            System.out.print("  Nhập username: ");
            String username = scanner.nextLine();
            System.out.print("  Nhập password: ");
            String password = scanner.nextLine();

            loggedInUser = userDAO.login(username, password);

            if (loggedInUser == null) {
                System.out.println("\n  [X] Sai tài khoản hoặc mật khẩu! Vui lòng thử lại.\n");
            }
        }

        System.out.println("\n  [✓] Đăng nhập thành công! Xin chào, " + loggedInUser.getUsername());

        if (loggedInUser.isAdmin()) {
           adminView.displayAdminView(bookDAO);          
        } else {
            userView.displayUserView(bookDAO);
        }
        scanner.close();
    }
}