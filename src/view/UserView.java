package view;

import java.util.List;
import java.util.Scanner;

import dao.BookDAO;
import model.Book;

public class UserView {

    Scanner scanner = new Scanner(System.in);

    public void displayUserView(BookDAO bookDAO) {
        boolean isUserRunning = true;
        while (isUserRunning) {
            displayMenu();

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Vui lòng nhập một số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1:
                    handleSearchBook(bookDAO);
                    break;
                case 2:
                    handleCategoryFilter();
                    break;
                case 3:
                    handleCartReview();
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

    private void displayMenu() {
        System.out.println("\n╔════════════════ MENU CHỨC NĂNG ════════════════╗");
        System.out.println("║  1. Tìm kiếm sách                              ║");
        System.out.println("║  2. Lọc thể loại                               ║");
        System.out.println("║  3. Xem giỏ hàng                               ║");
        System.out.println("║  0. Đăng xuất                                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print(" ➜ Mời bạn chọn chức năng (0-3): ");
    }

    private void displaySubMenu() {
        System.out.println("\n┌──────────────── TUỲ CHỌN ────────────────┐");
        System.out.println("│ 1. Xem chi tiết sách                     │");
        System.out.println("│ 2. Thêm sách vào giỏ hàng                │");
        System.out.println("│ 0. Quay lại menu chính                   │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print(" ➜ Mời bạn chọn (0-2): ");
    }

    private void displayBookBySearch(List<Book> foundBooks) {
        System.out.println("\n╔═════════════════════ KẾT QUẢ TÌM KIẾM SÁCH ════════════════════╗");
        for (Book b : foundBooks) {
            System.out.printf("║ %-15s: %-45s ║%n", "ID", b.getId());
            System.out.printf("║ %-15s: %-45s ║%n", "Tên sách", b.getTitle());
            System.out.printf("║ %-15s: %-45s ║%n", "Tác giả", b.getAuthorName());
            System.out.printf("║ %-15s: %-45s ║%n", "Ngày ra mắt", b.getCreatedAt().toString());
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
        }
    }

    private void displayDetailBook(int detailId, Book detailBook) {
        if (detailBook != null) {
            System.out.println("\n╔════════════════════ THÔNG TIN CHI TIẾT SÁCH ═══════════════════╗");
            System.out.printf("║ %-15s: %-45s ║\n", "Tên sách", detailBook.getTitle());
            System.out.printf("║ %-15s: %-45s ║\n", "Tác giả", detailBook.getAuthorName());
            System.out.printf("║ %-15s: %-45s ║\n", "Nhà xuất bản", detailBook.getPublisherName());
            System.out.printf("║ %-15s: %-45s ║\n", "Thể loại", (detailBook.getCategories() != null ? detailBook.getCategories() : "Chưa cập nhật"));
            System.out.printf("║ %-15s: %,.0f VNĐ%-34s ║\n", "Giá", detailBook.getPrice(), "");
            System.out.printf("║ %-15s: %-45d ║\n", "Số trang", detailBook.getPageCount());
            System.out.printf("║ %-15s: %-45d ║\n", "Tồn kho", detailBook.getStockQuantity());
            System.out.println("╠═══════════════════════════════ Mô tả ══════════════════════════╣");

            // Sử dụng thuật toán ngắt dòng đơn giản nếu mô tả quá dài
            String desc = detailBook.getDescription();
            if (desc != null) {
                int wrapLength = 60;
                for (int i = 0; i < desc.length(); i += wrapLength) {
                    System.out.printf("║ %-62s ║\n", desc.substring(i, Math.min(desc.length(), i + wrapLength)));
                }
            } else {
                System.out.printf("║ %-62s ║\n", "Đang cập nhật");
            }

            System.out.println(
                    "╚════════════════════════════════════════════════════════════════╝");

        } else {
            System.out.println("  [!] Không tìm thấy sách với ID: " + detailId);
        }
    }

    private void handleSearchBook(BookDAO bookDAO) {
        System.out.println("\n┌─────────────────── TÌM KIẾM SÁCH ────────────────────┐");
        System.out.print("  Nhập tên sách cần tìm (Nhấn 0 để quay lại): ");
        String keyword = scanner.nextLine();
        System.out.println("└──────────────────────────────────────────────────────┘");

        if (keyword.equals("0")) {
            return;
        }

        List<Book> foundBooks = bookDAO.searchBooksByName(keyword);

        if (foundBooks.isEmpty()) {
            System.out.println("  [!] Không tìm thấy quyển sách nào phù hợp.");
        } else {
            // Hiển thị kết quả với id, title, author_name, created_at
            displayBookBySearch(foundBooks);

            boolean isSubMenuRunning = true;
            while (isSubMenuRunning) {
                displaySubMenu();

                String subChoice = scanner.nextLine();

                switch (subChoice) {
                    case "1":
                        handleDetailSelection(bookDAO, foundBooks);
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

    private void handleCategoryFilter() {
        System.out.println("\n┌──────────────── LỌC THỂ LOẠI ────────────────┐");
        System.out.println("│ Tính năng đang được xây dựng...              │");
        System.out.println("└──────────────────────────────────────────────┘");
    }

    private void handleCartReview() {
        System.out.println("\n┌────────────── GIỎ HÀNG CỦA BẠN ──────────────┐");
        System.out.println("│ Tính năng đang được xây dựng...              │");
        System.out.println("└──────────────────────────────────────────────┘");
    }

    private void handleDetailSelection(BookDAO bookDAO, List<Book> foundBooks) {
        if (foundBooks.size() == 1) {
            Book soleBook = bookDAO.getBookById(foundBooks.get(0).getId());
            displayDetailBook(soleBook.getId(), soleBook);
        } else {
            System.out.print(" ➜ Nhập ID sách muốn xem chi tiết: ");
            String detailIdStr = scanner.nextLine();

            try {
                int detailId = Integer.parseInt(detailIdStr);
                Book detailBook = bookDAO.getBookById(detailId);

                displayDetailBook(detailId, detailBook);
                
            } catch (NumberFormatException e) {
                System.out.println("  [!] ID sách phải là một số hợp lệ!");
            }
        }
    }

    private void handleAddToCartSelection() {
        System.out.print(" ➜ Nhập ID sách muốn thêm vào giỏ: ");
        String cartBookIdStr = scanner.nextLine();
        System.out.print(" ➜ Nhập số lượng: ");
        // String quantityStr = scanner.nextLine();
        // TODO: Xử lý thêm vào giỏ hàng
        System.out.println("  [*] Đang xây dựng chức năng thêm giỏ hàng cho ID: " + cartBookIdStr);
    }
    
}
