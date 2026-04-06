package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class BookDAO {

    // Hàm tìm kiếm sách theo tên, kết quả trả về là id, title, author_name,
    // created_at
    public List<Book> searchBooksByName(String keyword) {
        List<Book> bookList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                CallableStatement cStmt = conn.prepareCall("{call sp_SearchBooksByName(?)}")) {

            cStmt.setString(1, keyword);
            ResultSet rs = cStmt.executeQuery();

            return mapBookList(rs);
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm sách: " + e.getMessage());
            e.printStackTrace();
        }
        return bookList;
    }

    // Hàm chi tiết sách trả về kết quả là id, title, price, description,
    // page_count, stock_quantity, created_at
    public Book getBookById(int id) {
        Book book = null;
        // Câu SQL JOIN các bảng book, author, publisher và category
        String query = "SELECT b.id, b.title, b.price, b.description, b.page_count, b.stock_quantity, b.created_at, " +
                "a.name AS author_name, p.name AS publisher_name, c.name AS category_name " +
                "FROM book b " +
                "JOIN author a ON b.id_author = a.id " +
                "JOIN publisher p ON b.id_publisher = p.id " +
                "JOIN category c ON b.id_category = c.id " +
                "WHERE b.id = ? ";

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
                book.setCategory(rs.getString("category_name"));
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết sách: " + e.getMessage());
            e.printStackTrace();
        }
        return book;
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> bookList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                CallableStatement cStmt = conn.prepareCall("{call sp_GetBooksByCategory(?)}")) {

            cStmt.setString(1, category);
            ResultSet rs = cStmt.executeQuery();
            return mapBookList(rs);

        } catch (Exception e) {
            System.out.println("Lỗi khi tìm kiếm sách: " + e.getMessage());
            e.printStackTrace();
        }

        return bookList;
    }

    private List<Book> mapBookList(ResultSet rs) throws Exception {
        List<Book> list = new ArrayList<>();

        while (rs.next()) {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthorName(rs.getString("author_name"));
            book.setCreatedAt(rs.getTimestamp("created_at"));
            list.add(book);
        }

        return list;
    }

}