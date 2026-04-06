package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Types;

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

    // Hàm lấy danh sách tất cả các sách - Admin
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_GetAllBooks()}")) {

            ResultSet rs = cStmt.executeQuery();
            bookList = mapBookList(rs); // Tái sử dụng hàm mapBookList đã có

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách sách: " + e.getMessage());
            e.printStackTrace();
        }
        return bookList;
    }

    // Lấy danh sách dữ liệu (Dùng chung cho Author, Category, Publisher) - Admin
    public Map<Integer, String> getSimpleList(String spName) {
        Map<Integer, String> list = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call " + spName + "()}")) {
            ResultSet rs = cStmt.executeQuery();
            while (rs.next()) {
                list.put(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách: " + e.getMessage());
        }
        return list;
    }

    // Thêm Tác giả và lấy lại ID - Admin
    public int insertAuthorAndGetId(String name, String bio) {
        int newId = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_InsertAuthor(?, ?, ?)}")) {
            cStmt.setString(1, name);
            cStmt.setString(2, bio);
            cStmt.registerOutParameter(3, Types.INTEGER); // Lấy tham số OUT
            cStmt.execute();
            newId = cStmt.getInt(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }
    // Thêm Nhà xuất bản và lấy lại ID - Admin
    public int insertPublisherAndGetId(String name) {
        int newId = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_InsertPublisher(?, ?)}")) {
            cStmt.setString(1, name);
            cStmt.registerOutParameter(2, Types.INTEGER);
            cStmt.execute();
            newId = cStmt.getInt(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    // Thêm Sách - Admin
    public boolean insertBook(int authorId, int publisherId, int categoryId,
                              double price, String title, String description, int stock, int pages) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_InsertBook(?, ?, ?, ?, ?, ?, ?, ?)}")) {
            cStmt.setInt(1, authorId);
            cStmt.setInt(2, publisherId);
            cStmt.setInt(3, categoryId);
            cStmt.setDouble(4, price);
            cStmt.setString(5, title);
            cStmt.setString(6, description);
            cStmt.setInt(7, stock);
            cStmt.setInt(8, pages);
            cStmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm sách: " + e.getMessage());
            return false;
        }
    }

    // Chỉnh sửa thông tin Sách - Admin
    public boolean updateBook(int bookId, int authorId, int publisherId, int categoryId,
                              double price, String title, String description, int stock, int pages) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_UpdateBook(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            cStmt.setInt(1, bookId);
            cStmt.setInt(2, authorId);
            cStmt.setInt(3, publisherId);
            cStmt.setInt(4, categoryId);
            cStmt.setDouble(5, price);
            cStmt.setString(6, title);
            cStmt.setString(7, description);
            cStmt.setInt(8, stock);
            cStmt.setInt(9, pages);

            // Dùng executeUpdate() cho các thao tác INSERT, UPDATE, DELETE
            int rowsAffected = cStmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật thành công

        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật sách: " + e.getMessage());
            return false;
        }
    }
    // Xóa mềm Sách - Admin
    public boolean deleteBook(int bookId) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cStmt = conn.prepareCall("{call sp_SoftDeleteBook(?)}")) {

            cStmt.setInt(1, bookId);

            int rowsAffected = cStmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Lỗi khi xóa sách: " + e.getMessage());
            return false;
        }
    }
}
