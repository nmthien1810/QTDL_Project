package view;

import java.util.Scanner;

import dao.BookDAO;

public class AdminView {

    Scanner scanner = new Scanner(System.in);

    public void displayAdminView(BookDAO bookDAO) {
        displayMenu();
    }

    // private void displayMenu() {
    //     System.out.println("\n╔════════════════ MENU CHỨC NĂNG ════════════════╗");
    //     System.out.println("║  1. Thêm sách                             ║");
    //     System.out.println("║  2. Chỉnh sửa sách                     ║");
    //     System.out.println("║  3. Xem giỏ hàng                               ║");
    //     System.out.println("║  0. Đăng xuất                                  ║");
    //     System.out.println("╚════════════════════════════════════════════════╝");
    //     System.out.print(" ➜ Mời bạn chọn chức năng (0-3): ");
    // }

    private void displayMenu() {
        System.out.printf("\n╔════════════════%-16s════════════════╗", " MENU CHỨC NĂNG ");
        System.out.printf("\n║%-48s║", "  1. Thêm sách");
        System.out.printf("\n║%-48s║", "  2. Chỉnh sửa sách");
        System.out.printf("\n║%-48s║", "  3. Tạo đơn hàng");
        System.out.printf("\n║%-48s║", "  0. Đằng xuất");
        System.out.println("\n╚════════════════════════════════════════════════╝");

    }
}
