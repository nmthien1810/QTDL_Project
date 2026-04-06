package view;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import dao.BookDAO;
import display.UserDisplay;
import model.Book;
import model.CartItem;

public class UserView {

    Scanner scanner = new Scanner(System.in);

    UserDisplay user = new UserDisplay();

    // In-memory cart (mảng)
    private List<CartItem> cart = new ArrayList<>();

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

    // hàm xử lý cho tìm kiếm sách theo tên, theo thể loại
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
                        if (books.size() == 1)
                            handleAddToCartSelection();
                        else
                            handl
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
        if (cart.isEmpty()) {
            System.out.println("\n┌────────────── GIỎ HÀNG CỦA BẠN ──────────────┐");
            System.out.println("│ Giỏ hàng trống                               │");
            System.out.println("└──────────────────────────────────────────────┘");
            return;
        }

        System.out.println("\n╔════════════════ GIỎ HÀNG ════════════════╗");
        double total = 0.0;
        int idx = 1;
        for (CartItem item : cart) {
            System.out.printf("║ %d. %-30s x%-3d %10.0f VNĐ ║%n", idx++, item.getTitle(), item.getQuantity(), item.getTotal());
            total += item.getTotal();
        }
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.printf("║ %-40s %10.0f VNĐ ║%n", "TỔNG CỘNG:", total);
        System.out.println("╚═════════════════════════════════════════╝");

        System.out.println("\n1. Thanh toán    0. Quay lại");
        System.out.print(" ➜ Chọn: ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            proceedCheckout();
        }
    }

    // xử lý chi tiết sách được chọn
    private void handleDetailSelection(BookDAO bookDAO, List<Book> foundBooks) {
        Book targetBook = null;
        int detailId = -1;

        if (foundBooks.size() == 1) {
            targetBook = bookDAO.getBookById(foundBooks.get(0).getId());
        } else {
            System.out.print(" ➜ Nhập ID sách muốn xem chi tiết: ");
            try {
                detailId = Integer.parseInt(scanner.nextLine());

                boolean exists = false;
                for (Book b : foundBooks) {
                    if (b.getId() == detailId) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    System.out.println("  [!] ID sách này không nằm trong kết quả tìm kiếm.");
                    return;
                }

                targetBook = bookDAO.getBookById(detailId);
            } catch (NumberFormatException e) {
                System.out.println("  [!] ID sách phải là một số hợp lệ!");
                return;
            }
        }

        if (targetBook != null) {
            showDetailBook(targetBook);
        } else {
            System.out.println("  [!] Không tìm thấy thông tin sách!");
        }
    }
    
    // hiển thị chi tiết sách 
    private void showDetailBook(Book book) {
        user.displayDetailBook(book.getId(), book);
        // hiển thị detailSubmenu để thêm sách vào giỏ hàng hoặc quay lại
        boolean isDetailSubMenuRunning = true;
        while (isDetailSubMenuRunning) {
            user.displayDetailSubMenu();

            String detailSubChoice = scanner.nextLine();

            switch (detailSubChoice) {
                case "1":
                    // thêm vào giỏ với ID hiện tại
                    handleAddToCartSelection(book.getId());
                    break;
                case "0":
                    isDetailSubMenuRunning = false;
                    break;
                default:
                    System.out.println("  [!] Lựa chọn không hợp lệ!");
            }
        }
    }

    // thêm vào giỏ hàng từ sau khi tìm kiếm
    private void handleAddToCartSelection() {
        System.out.print(" ➜ Nhập ID sách muốn thêm vào giỏ: ");
        String cartBookIdStr = scanner.nextLine();
        int bookId;
        try {
            bookId = Integer.parseInt(cartBookIdStr.trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] ID không hợp lệ.");
            return;
        }
        System.out.print(" ➜ Nhập số lượng: ");
        String qtyStr = scanner.nextLine();
        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  [!] Số lượng không hợp lệ.");
            return;
        }
        addToCart(bookId, qty);
    }

    // thêm vào giỏ hàng khi xem chi tiết sách
    private void handleAddToCartSelection(int bookId) {
        System.out.println(" ➜ Thêm sách có ID: " + bookId + " vào giỏ");
        System.out.print(" ➜ Nhập số lượng: ");
        String qtyStr = scanner.nextLine();
        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  [!] Số lượng không hợp lệ.");
            return;
        }
        addToCart(bookId, qty);
    }

    // Thêm vào giỏ (logic chung)
    private void addToCart(int bookId, int qty) {
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("  [!] Không tìm thấy sách với ID: " + bookId);
            return;
        }
        if (qty > book.getStockQuantity()) {
            System.out.println("  [!] Số lượng vượt quá tồn kho (" + book.getStockQuantity() + ").");
            return;
        }

        for (CartItem item : cart) {
            if (item.getBookId() == bookId) {
                int newQty = item.getQuantity() + qty;
                if (newQty > book.getStockQuantity()) {
                    System.out.println("  [!] Tổng số lượng trong giỏ vượt quá tồn kho.");
                    return;
                }
                item.setQuantity(newQty);
                System.out.println("  [✓] Đã cập nhật số lượng trong giỏ: " + item.getTitle() + " x" + item.getQuantity());
                return;
            }
        }

        CartItem newItem = new CartItem(bookId, book.getTitle(), qty, book.getPrice());
        cart.add(newItem);
        System.out.println("  [✓] Đã thêm vào giỏ: " + book.getTitle() + " x" + qty);
    }

    // Thanh toán
    private void proceedCheckout() {
        BookDAO bookDAO = new BookDAO();
        // kiểm tra tồn kho lần cuối
        for (CartItem item : cart) {
            Book book = bookDAO.getBookById(item.getBookId());
            if (book == null) {
                System.out.println("  [!] Sách không tồn tại: " + item.getBookId());
                return;
            }
            if (item.getQuantity() > book.getStockQuantity()) {
                System.out.println("  [!] Tồn kho không đủ cho: " + item.getTitle());
                return;
            }
        }

        // cập nhật DB: giảm tồn kho
        for (CartItem item : cart) {
            boolean ok = bookDAO.reduceStock(item.getBookId(), item.getQuantity());
            if (!ok) {
                System.out.println("  [!] Lỗi khi cập nhật tồn kho cho: " + item.getTitle());
                return;
            }
        }

        double total = 0.0;
        for (CartItem item : cart) total += item.getTotal();
        cart.clear();
        System.out.printf("\n  [✓] Thanh toán thành công. Tổng: %,.0f VNĐ\n", total);
    }

}
