package view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import dao.BookDAO;
import display.AdminDisplay; // Import thêm AdminDisplay
import model.Book;

public class AdminView {
    Scanner scanner = new Scanner(System.in);
    AdminDisplay adminDisplay = new AdminDisplay(); // Khởi tạo đối tượng hiển thị

    public void displayAdminView(BookDAO bookDAO) {
        boolean isAdminRunning = true;
        while (isAdminRunning) {
            adminDisplay.displayMenu(); // Gọi menu từ AdminDisplay
            System.out.print(" ➜ Chọn chức năng: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleAddBook(bookDAO);
                    break;
                case "2":
                    handleDeleteBook(bookDAO);
                    break;
                case "3":
                    handleEditBook(bookDAO);
                    break;
                case "0":
                    isAdminRunning = false;
                    adminDisplay.printSystemMessage("[✓] Đã đăng xuất tài khoản Admin!");
                    break;
                default:
                    adminDisplay.printSystemMessage("[!] Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleAddBook(BookDAO bookDAO) {
        adminDisplay.printAddBookHeader();

        // 1. Xử lý Tác giả
        adminDisplay.printAuthorSection();
        adminDisplay.printInputPrompt("Chọn (1/2): ");
        int authorId = -1;
        String authorChoice = scanner.nextLine();

        if (authorChoice.equals("1")) {
            adminDisplay.printInputPrompt("Nhập tên tác giả: ");
            String authorName = scanner.nextLine();
            adminDisplay.printInputPrompt("Nhập tiểu sử (bio): ");
            String authorBio = scanner.nextLine();
            authorId = bookDAO.insertAuthorAndGetId(authorName, authorBio);
        } else {
            Map<Integer, String> authors = bookDAO.getSimpleList("sp_GetAllAuthors");
            // Gọi hàm in danh sách có đóng khung
            adminDisplay.displayBoxedList("Danh sách tác giả", authors);
            adminDisplay.printInputPrompt("Nhập ID tác giả: ");
            authorId = Integer.parseInt(scanner.nextLine());
        }

        // 2. Xử lý Thể loại
        adminDisplay.printCategorySection();
        Map<Integer, String> categories = bookDAO.getSimpleList("sp_GetAllCategories");
        // Gọi hàm in danh sách có đóng khung
        adminDisplay.displayBoxedList("Danh sách thể loại", categories);
        adminDisplay.printInputPrompt("Nhập ID thể loại: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        // 3. Xử lý Nhà xuất bản
        adminDisplay.printPublisherSection();
        adminDisplay.printInputPrompt("Chọn (1/2): ");
        int publisherId = -1;
        String pubChoice = scanner.nextLine();

        if (pubChoice.equals("1")) {
            adminDisplay.printInputPrompt("Nhập tên NXB: ");
            String pubName = scanner.nextLine();
            publisherId = bookDAO.insertPublisherAndGetId(pubName);
        } else {
            Map<Integer, String> publishers = bookDAO.getSimpleList("sp_GetAllPublishers");
            // Gọi hàm in danh sách có đóng khung
            adminDisplay.displayBoxedList("Danh sách nhà xuất bản", publishers);
            adminDisplay.printInputPrompt("Nhập ID NXB: ");
            publisherId = Integer.parseInt(scanner.nextLine());
        }

        // 4. Nhập thông tin sách chi tiết (Không khung)
        adminDisplay.printBookDetailSection();
        adminDisplay.printInputPrompt("Tên sách (title): ");
        String title = scanner.nextLine();
        adminDisplay.printInputPrompt("Mô tả (description): ");
        String description = scanner.nextLine();
        adminDisplay.printInputPrompt("Số lượng (stock_quantity): ");
        int stock = Integer.parseInt(scanner.nextLine());
        adminDisplay.printInputPrompt("Số trang (page_count): ");
        int pages = Integer.parseInt(scanner.nextLine());
        adminDisplay.printInputPrompt("Giá (price): ");
        double price = Double.parseDouble(scanner.nextLine());

        // 5. Xác nhận (Không khung)
        adminDisplay.printConfirmSection();
        adminDisplay.printInputPrompt("Lựa chọn: ");
        String confirm = scanner.nextLine();

        System.out.println(); // In dòng trống cho thoáng

        if (confirm.equals("1")) {
            boolean success = bookDAO.insertBook(authorId, publisherId, categoryId, price, title, description, stock, pages);
            if (success) {
                adminDisplay.printSystemMessage("[✓] Đã thêm sách thành công vào cơ sở dữ liệu!");
            } else {
                adminDisplay.printSystemMessage("[X] Lỗi khi thêm sách vào CSDL.");
            }
        } else {
            adminDisplay.printSystemMessage("[X] Đã hủy thao tác thêm sách.");
        }
    }


    private void handleEditBook(BookDAO bookDAO) {
        adminDisplay.printEditBookHeader();

        List<Book> allBooks = bookDAO.getAllBooks();
        adminDisplay.displayAllBooks(allBooks);

        // BƯỚC QUAN TRỌNG: Lấy ID sách cần sửa
        adminDisplay.printInputPrompt("Nhập ID sách cần chỉnh sửa (Nhấn 0 để hủy): ");
        int bookId = Integer.parseInt(scanner.nextLine());

        if (bookId == 0) {
            adminDisplay.printSystemMessage("[*] Đã hủy thao tác chỉnh sửa.");
            return;
        }

        // Kiểm tra xem sách có tồn tại không trước khi bắt admin nhập một đống thông tin
        Book existingBook = bookDAO.getBookById(bookId);
        if (existingBook == null) {
            adminDisplay.printSystemMessage("[X] Không tìm thấy sách với ID: " + bookId);
            return;
        }

        // 1. Xử lý Tác giả
        adminDisplay.printAuthorSection();
        adminDisplay.printInputPrompt("Chọn (1/2): ");
        int authorId = -1;
        String authorChoice = scanner.nextLine();
        if (authorChoice.equals("1")) {
            adminDisplay.printInputPrompt("Nhập tên tác giả: ");
            String authorName = scanner.nextLine();
            adminDisplay.printInputPrompt("Nhập tiểu sử (bio): ");
            String authorBio = scanner.nextLine();
            authorId = bookDAO.insertAuthorAndGetId(authorName, authorBio);
        } else {
            Map<Integer, String> authors = bookDAO.getSimpleList("sp_GetAllAuthors");
            adminDisplay.displayBoxedList("Danh sách tác giả", authors);
            adminDisplay.printInputPrompt("Nhập ID tác giả: ");
            authorId = Integer.parseInt(scanner.nextLine());
        }

        // 2. Xử lý Thể loại
        adminDisplay.printCategorySection();
        Map<Integer, String> categories = bookDAO.getSimpleList("sp_GetAllCategories");
        adminDisplay.displayBoxedList("Danh sách thể loại", categories);
        adminDisplay.printInputPrompt("Nhập ID thể loại: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        // 3. Xử lý Nhà xuất bản
        adminDisplay.printPublisherSection();
        adminDisplay.printInputPrompt("Chọn (1/2): ");
        int publisherId = -1;
        String pubChoice = scanner.nextLine();
        if (pubChoice.equals("1")) {
            adminDisplay.printInputPrompt("Nhập tên NXB: ");
            String pubName = scanner.nextLine();
            publisherId = bookDAO.insertPublisherAndGetId(pubName);
        } else {
            Map<Integer, String> publishers = bookDAO.getSimpleList("sp_GetAllPublishers");
            adminDisplay.displayBoxedList("Danh sách nhà xuất bản", publishers);
            adminDisplay.printInputPrompt("Nhập ID NXB: ");
            publisherId = Integer.parseInt(scanner.nextLine());
        }

        // 4. Nhập thông tin sách chi tiết
        adminDisplay.printBookDetailSection();
        adminDisplay.printInputPrompt("Tên sách mới: ");
        String title = scanner.nextLine();
        adminDisplay.printInputPrompt("Mô tả mới: ");
        String description = scanner.nextLine();
        adminDisplay.printInputPrompt("Số lượng mới: ");
        int stock = Integer.parseInt(scanner.nextLine());
        adminDisplay.printInputPrompt("Số trang mới: ");
        int pages = Integer.parseInt(scanner.nextLine());
        adminDisplay.printInputPrompt("Giá mới: ");
        double price = Double.parseDouble(scanner.nextLine());

        // 5. Xác nhận
        adminDisplay.printConfirmSection();
        adminDisplay.printInputPrompt("Lựa chọn (1: Xác nhận, 0: Hủy): ");
        String confirm = scanner.nextLine();
        System.out.println();

        if (confirm.equals("1")) {
            // Gọi hàm updateBook thay vì insertBook
            boolean success = bookDAO.updateBook(bookId, authorId, publisherId, categoryId, price, title, description, stock, pages);
            if (success) {
                adminDisplay.printSystemMessage("[✓] Đã cập nhật thông tin sách thành công!");
            } else {
                adminDisplay.printSystemMessage("[X] Lỗi khi cập nhật sách vào CSDL.");
            }
        } else {
            adminDisplay.printSystemMessage("[X] Đã hủy thao tác chỉnh sửa sách.");
        }
    }
    private void handleDeleteBook(BookDAO bookDAO) {
        adminDisplay.printDeleteBookHeader();

        // 1. In danh sách sách cho Admin xem
        List<Book> allBooks = bookDAO.getAllBooks();
        adminDisplay.displayAllBooks(allBooks);

        // 2. Nhập ID sách cần xóa
        adminDisplay.printInputPrompt("Nhập ID sách cần xóa (Nhấn 0 để hủy): ");
        int bookId = Integer.parseInt(scanner.nextLine());

        if (bookId == 0) {
            adminDisplay.printSystemMessage("[*] Đã hủy thao tác xóa sách.");
            return;
        }

        // 3. Kiểm tra sách có tồn tại không
        Book targetBook = bookDAO.getBookById(bookId);
        if (targetBook == null) {
            adminDisplay.printSystemMessage("[X] Không tìm thấy sách với ID: " + bookId);
            return;
        }

        // 4. Xác nhận trước khi xóa (Rất quan trọng trong UX)
        System.out.println("  [!] Bạn đang chuẩn bị xóa sách: " + targetBook.getTitle());
        adminDisplay.printInputPrompt("Bạn có chắc chắn muốn xóa? (1: Có, 0: Không): ");
        String confirm = scanner.nextLine();

        if (confirm.equals("1")) {
            boolean success = bookDAO.deleteBook(bookId);
            if (success) {
                adminDisplay.printSystemMessage("[✓] Đã xóa sách thành công (Xóa mềm)!");
            } else {
                adminDisplay.printSystemMessage("[X] Có lỗi xảy ra trong quá trình xóa.");
            }
        } else {
            adminDisplay.printSystemMessage("[*] Đã hủy thao tác xóa sách.");
        }
    }
}
