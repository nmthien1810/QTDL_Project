package display;

import java.util.List;
import java.util.Map;

import model.Book;

public class AdminDisplay {

    // Menu chính của Admin (Giữ nguyên khung)
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════ MENU CHỨC NĂNG ═════════════════════════════╗");
        System.out.println("║  1. Thêm sách                                                           ║");
        System.out.println("║  2. Xóa sách                                                            ║");
        System.out.println("║  3. Chỉnh sửa sách                                                      ║");
        System.out.println("║  0. Đăng xuất                                                           ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
    }

    // Các tiêu đề phân mục (Bỏ khung, in text thuần)
    public void printAddBookHeader() {
        System.out.println("\n--- THÊM SÁCH MỚI ---");
    }
    public void printEditBookHeader() {
        System.out.println("\n--- CHỈNH SỬA THÔNG TIN SÁCH ---");
    }
    public void printDeleteBookHeader() {
        System.out.println("\n--- XÓA SÁCH ---");
    }
    public void printAuthorSection() {
        System.out.println("\n1. THÔNG TIN TÁC GIẢ");
        System.out.println("1: Thêm tác giả mới | 2: Chọn tác giả có sẵn");
    }

    public void printCategorySection() {
        System.out.println("\n2. THỂ LOẠI SÁCH");
    }

    public void printPublisherSection() {
        System.out.println("\n3. THÔNG TIN NHÀ XUẤT BẢN");
        System.out.println("1: Thêm NXB mới | 2: Chọn NXB có sẵn");
    }

    public void printBookDetailSection() {
        System.out.println("\n4. THÔNG TIN CHI TIẾT SÁCH");
    }

    public void printConfirmSection() {
        System.out.println("\n--- XÁC NHẬN ---");
        System.out.println("Nhấn 1 để xác nhận thêm | Nhấn 0 để hủy và trở về");
    }

    // Hàm MỚI: Tự động đóng khung một danh sách bất kỳ kèm Tiêu đề
    public void displayBoxedList(String title, Map<Integer, String> list) {
        System.out.println("  ╔═════════════════════════════════════════════════════════════════════╗");
        System.out.printf("  ║ %-67s ║\n", title.toUpperCase());
        System.out.println("  ╠═════════════════════════════════════════════════════════════════════╣");
        list.forEach((id, name) -> System.out.printf("  ║ ID: %-4d - %-56s ║\n", id, name));
        System.out.println("  ╚═════════════════════════════════════════════════════════════════════╝");
    }

    // Yêu cầu nhập liệu (Không khung)
    public void printInputPrompt(String prompt) {
        System.out.print("➜ " + prompt);
    }

    // In thông báo hệ thống
    public void printSystemMessage(String message) {
        System.out.println(" " + message);
    }
    // Hiển thị toàn bộ sách với khung vuông vức
    public void displayAllBooks(List<Book> books) {
        // Dòng viền trên: 1 + 43 (═) + 23 (Text) + 43 (═) + 1 = 111
        System.out.println("\n╔═══════════════════════════════════════════ DANH SÁCH TẤT CẢ SÁCH ═══════════════════════════════════════════╗");

        if (books.isEmpty()) {
            System.out.printf("║ %-107s ║%n", "Không có sách nào trong cơ sở dữ liệu.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                System.out.printf("║ %-15s: %-90s ║%n", "ID", b.getId());
                System.out.printf("║ %-15s: %-90s ║%n", "Tên sách", b.getTitle());
                System.out.printf("║ %-15s: %-90s ║%n", "Tác giả", b.getAuthorName());

                String dateStr = (b.getCreatedAt() != null) ? b.getCreatedAt().toString() : "Đang cập nhật";
                System.out.printf("║ %-15s: %-90s ║%n", "Ngày ra mắt", dateStr);

                if (i < books.size() - 1) {
                    System.out.println("╠═════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
                }
            }
        }
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
    }
}
