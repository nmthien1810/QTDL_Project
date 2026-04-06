package view;

import java.util.List;
import java.util.Scanner;

import dao.BookDAO;
import display.UserDisplay;
import model.Book;

public class UserView {

    Scanner scanner = new Scanner(System.in);

    UserDisplay user = new UserDisplay();

    public void displayUserView(BookDAO bookDAO) {
        boolean isUserRunning = true;
        while (isUserRunning) {
            user.displayMenu();

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Vui lòng nhập một số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1:
                    handleSearchBook(bookDAO); // tìm kiếm sách
                    break;
                case 2:
                    handleCategoryFilter(bookDAO); // lọc thể loại
                    break;
                case 3:
                    handleCartReview(); // xem giỏ hàng
                    break;
                case 0:
                    isUserRunning = false;
                    System.out.println("\n  [✓] Đã đăng xuất tài khoản! Tạm biệt.");
                    break;
                default:
                    System.out.println("  [!] Lựa chọn không hợp lệ. Vui lòng thử lại!");
            }
        }
    }

    

    // hàm xử lý cho case 1 và 2
    private void processBookListResults(BookDAO bookDAO, List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("  [!] Không tìm thấy quyển sách nào phù hợp.");
            // hiển thị lại menu chính

        } else {
            // Hiển thị kết quả với id, title, author_name, created_at
            user.displayBookBySearch(books);

            boolean isSubMenuRunning = true;
            while (isSubMenuRunning) {
                user.displaySubMenu();

                String subChoice = scanner.nextLine();

                switch (subChoice) {
                    case "1":
                        handleDetailSelection(bookDAO, books);
                        break;
                    case "2":
                        handleAddToCartSelection();
                        break;
                    case "0":
                        isSubMenuRunning = false;
                        break;
                    default:
                        System.out.println("  [!] Lựa chọn không hợp lệ!");
                }
            }
        }

    }

    // tìm kiếm sách case 1
    private void handleSearchBook(BookDAO bookDAO) {

        String keyword = "";

        while (true) {
            System.out.println("\n┌─────────────────── TÌM KIẾM SÁCH ────────────────────┐");
            System.out.print("  Nhập tên sách cần tìm (Nhấn 0 để quay lại): ");
            keyword = scanner.nextLine().trim();
            System.out.println("└──────────────────────────────────────────────────────┘");

            if (!keyword.isEmpty()) {
                break;
            }

            System.out.println("  Vui lòng nhập lại");
        }

        if (keyword.equals("0")) {
            return;
        }

        List<Book> foundBooks = bookDAO.searchBooksByName(keyword);

        processBookListResults(bookDAO, foundBooks);

    }

    // lọc thể loại case 2
    private void handleCategoryFilter(BookDAO bookDAO) {

        String keyword = "";

        while (true) {
            System.out.println("\n┌──────────────── LỌC SÁCH THEO THỂ LOẠI ─────────────────┐");
            System.out.print("  Nhập thể loại cần tìm (Nhấn 0 để quay lại): ");
            keyword = scanner.nextLine().trim();
            System.out.println("└───────────────────────────────────────────────────────┘");

            if (!keyword.isEmpty()) {
                break;
            }

            System.out.println("  Vui lòng nhập lại");
        }

        if (keyword.equals("0")) {
            return;
        }

        List<Book> foundBooks = bookDAO.getBooksByCategory(keyword);

        processBookListResults(bookDAO, foundBooks);

    }

    // xem giỏ hàng case 3
    private void handleCartReview() {
        System.out.println("\n┌────────────── GIỎ HÀNG CỦA BẠN ──────────────┐");
        System.out.println("│ Tính năng đang được xây dựng...              │");
        System.out.println("└──────────────────────────────────────────────┘");
    }

    // hiển thị chi tiết sách được chọn
    private void handleDetailSelection(BookDAO bookDAO, List<Book> foundBooks) {
        Book targetBook = null;

        if (foundBooks.size() == 1) {
            targetBook = bookDAO.getBookById(foundBooks.get(0).getId());
        } else {
            System.out.print(" ➜ Nhập ID sách muốn xem chi tiết: ");
            try {
                int detailId = Integer.parseInt(scanner.nextLine());
                targetBook = bookDAO.getBookById(detailId);
            } catch (NumberFormatException e) {
                System.out.println("  [!] ID sách phải là một số hợp lệ!");
                return;
            }
        }

        if (targetBook != null) {
            user.displayDetailBook(targetBook.getId(), targetBook);
        } else {
            System.out.println("  [!] Không tìm thấy thông tin sách!");
        }
    }

    // thêm vào giỏ hàng
    private void handleAddToCartSelection() {
        System.out.print(" ➜ Nhập ID sách muốn thêm vào giỏ: ");
        String cartBookIdStr = scanner.nextLine();
        System.out.print(" ➜ Nhập số lượng: ");
        // String quantityStr = scanner.nextLine();
        // TODO: Xử lý thêm vào giỏ hàng
        System.out.println("  [*] Đang xây dựng chức năng thêm giỏ hàng cho ID: " + cartBookIdStr);
    }

    // thêm vào giỏ hàng khi đã có ID (gọi từ menu chi tiết)
    private void handleAddToCartSelection(int preselectedBookId) {
        System.out.println(" ➜ Thêm sách có ID: " + preselectedBookId + " vào giỏ");
        System.out.print(" ➜ Nhập số lượng: ");
        // String quantityStr = scanner.nextLine();
        // TODO: Xử lý thêm vào giỏ hàng với preselectedBookId
        System.out.println("  [*] Đang xây dựng chức năng thêm giỏ hàng cho ID: " + preselectedBookId);
    }

}
