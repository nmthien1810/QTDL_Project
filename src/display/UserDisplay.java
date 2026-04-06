package display;

import java.util.List;

import model.Book;

public class UserDisplay {
    public void displayMenu() {
        System.out.println("\n╔════════════════ MENU CHỨC NĂNG ════════════════╗");
        System.out.println("║  1. Tìm kiếm sách                              ║");
        System.out.println("║  2. Lọc sách theo thể loại                     ║");
        System.out.println("║  3. Xem giỏ hàng                               ║");
        System.out.println("║  0. Đăng xuất                                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print(" ➜ Mời bạn chọn chức năng (0-3): ");
    }

    public void displaySubMenu() {
        System.out.println("\n┌──────────────── TUỲ CHỌN ────────────────┐");
        System.out.println("│ 1. Xem chi tiết sách                     │");
        System.out.println("│ 2. Thêm sách vào giỏ hàng                │");
        System.out.println("│ 0. Quay lại menu chính                   │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print(" ➜ Mời bạn chọn (0-2): ");
    }

    public void displayDetailSubMenu() {
        System.out.println("\n┌────────── CHI TIẾT SÁCH - TÙY CHỌN ──────────┐");
        System.out.println("│ 1. Thêm vào giỏ hàng                         │");
        System.out.println("│ 0. Quay lại                                  │");
        System.out.println("└──────────────────────────────────────────────┘");
        System.out.print(" ➜ Mời bạn chọn (0-1): ");
    }


    // hiển thị sách theo tìm kiếm
    public void displayBookBySearch(List<Book> foundBooks) {
        System.out.println("\n╔═════════════════════ KẾT QUẢ TÌM KIẾM SÁCH ════════════════════╗");
        for (Book b : foundBooks) {
            System.out.printf("║ %-15s: %-45s ║%n", "ID", b.getId());
            System.out.printf("║ %-15s: %-45s ║%n", "Tên sách", b.getTitle());
            System.out.printf("║ %-15s: %-45s ║%n", "Tác giả", b.getAuthorName());
            System.out.printf("║ %-15s: %-45s ║%n", "Ngày ra mắt", b.getCreatedAt().toString());
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
        }
    }

    // hiển thị chi tiết sách
    public void displayDetailBook(int detailId, Book detailBook) {
        if (detailBook != null) {
            System.out.println("\n╔════════════════════ THÔNG TIN CHI TIẾT SÁCH ═══════════════════╗");
            System.out.printf("║ %-15s: %-45s ║\n", "Tên sách", detailBook.getTitle());
            System.out.printf("║ %-15s: %-45s ║\n", "Tác giả", detailBook.getAuthorName());
            System.out.printf("║ %-15s: %-45s ║\n", "Nhà xuất bản", detailBook.getPublisherName());
            System.out.printf("║ %-15s: %-45s ║\n", "Thể loại", detailBook.getCategory());
            System.out.printf("║ %-15s: %,.0f VNĐ%-34s ║\n", "Giá", detailBook.getPrice(), "");
            System.out.printf("║ %-15s: %-45d ║\n", "Số trang", detailBook.getPageCount());
            System.out.printf("║ %-15s: %-45d ║\n", "Tồn kho", detailBook.getStockQuantity());
            System.out.println("╠════════════════════════════ MÔ TẢ ═════════════════════════════╣");

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
}
