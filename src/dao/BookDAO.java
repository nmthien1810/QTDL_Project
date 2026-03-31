package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class BookDAO {

    // Hàm tìm kiếm sách theo tên, kết quả trả về là id, title, author_name, created_at
    public List<Book> searchBooksByName(String keyword) {
        List<Book> bookList = new ArrayList<>();

        // Cần JOIN bảng book (b) và bảng author (a) để lấy tên tác giả
        String query = "SELECT b.id, b.title, a.name AS author_name, b.created_at " +
                "FROM book b " +
                "JOIN author a ON b.id_author = a.id " +
                "WHERE b.title LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Thêm % ở hai đầu để tìm kiếm chuỗi chứa từ khóa
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCreatedAt(rs.getTimestamp("created_at"));

                bookList.add(book);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm sách: " + e.getMessage());
            e.printStackTrace();
        }
        return bookList;
    }

    // Hàm chi tiết sách trả về kết quả là id, title, price, description, page_count, stock_quantity, created_at
    public Book getBookById(int id) {
        Book book = null;
        // Câu SQL JOIN các bảng book, author, publisher, book_category và category
        String query = "SELECT b.id, b.title, b.price, b.description, b.page_count, b.stock_quantity, b.created_at, " +
                "a.name AS author_name, p.name AS publisher_name, " +
                "GROUP_CONCAT(c.name SEPARATOR ', ') AS categories " +
                "FROM book b " +
                "JOIN author a ON b.id_author = a.id " +
                "JOIN publisher p ON b.id_publisher = p.id " +
                "LEFT JOIN book_category bc ON b.id = bc.id_book " +
                "LEFT JOIN category c ON bc.id_category = c.id " +
                "WHERE b.id = ? " +
                "GROUP BY b.id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id); // Gắn id người dùng nhập vào câu truy vấn
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author_name"));
                book.setPublisherName(rs.getString("publisher_name"));
                book.setPrice(rs.getDouble("price"));
                book.setDescription(rs.getString("description"));
                book.setPageCount(rs.getInt("page_count"));
                book.setStockQuantity(rs.getInt("stock_quantity"));
                book.setCreatedAt(rs.getTimestamp("created_at"));
                book.setCategories(rs.getString("categories"));
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết sách: " + e.getMessage());
            e.printStackTrace();
        }
        return book;
    }
}