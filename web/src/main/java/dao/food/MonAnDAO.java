package dao.food;

import context.DBContext;
import model.food.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến Món ăn.
 */
public class MonAnDAO {

    /**
     * Lấy danh sách các món ăn nổi bật để hiển thị trên Trang chủ.
     * Chỉ lấy các món có trạng thái 'Còn hàng' và giới hạn số lượng (ví dụ: 20 món)
     * sắp xếp ngẫu nhiên để giao diện luôn mới mẻ.
     * @return Danh sách đối tượng MonAn.
     */
    public List<MonAn> getMonAnTrangChu() {
        List<MonAn> list = new ArrayList<>();

        // Dùng TOP 20 để giới hạn dữ liệu, NEWID() để random thứ tự hiển thị
        String sql = "SELECT TOP 20 ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN "
                + "WHERE TRANGTHAI = N'Còn hàng' "
                + "ORDER BY NEWID()";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new MonAn(
                        rs.getInt("ID_MONAN"),
                        rs.getInt("ID_CUAHANG"),
                        rs.getInt("ID_LOAI"),
                        rs.getNString("TENMON"),
                        rs.getNString("TRANGTHAI"),
                        rs.getDouble("GIA"),
                        rs.getString("IMG")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm món ăn theo tên (Hỗ trợ tìm kiếm gần đúng, không phân biệt hoa thường).
     * * @param keyword Từ khóa tên món ăn cần tìm (ví dụ: "bún", "phở").
     * @return Danh sách các món ăn khớp với từ khóa và đang được bày bán.
     */
    public List<MonAn> searchByName(String keyword) {
        List<MonAn> list = new ArrayList<>();
        // Lấy đầy đủ các cột bao gồm cả cột IMG
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN WHERE TENMON LIKE ? AND TRANGTHAI = N'Còn hàng'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sử dụng % để tìm kiếm chuỗi con chứa từ khóa
            ps.setNString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG") // Đọc link ảnh từ database
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách món ăn theo ID của một cửa hàng cụ thể.
     * * @param storeId ID của cửa hàng cần lấy thực đơn.
     * @return Danh sách các món ăn thuộc cửa hàng đó.
     */
    public List<MonAn> getByStore(int storeId) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN WHERE ID_CUAHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG") // Đọc link ảnh từ database
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lọc danh sách các món ăn thuộc một danh mục (loại món ăn) cụ thể.
     * Chỉ hiển thị các món ăn đang ở trạng thái 'Còn hàng'.
     * * @param idLoai Mã định danh duy nhất của loại món ăn cần lọc (ví dụ: ID của Đồ uống, Món chính).
     * @return Danh sách các đối tượng {@link MonAn} thuộc loại món ăn đó.
     */
    public List<MonAn> getByCategory(int idLoai) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN WHERE ID_LOAI = ? AND TRANGTHAI = N'Còn hàng'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLoai);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lọc danh sách món ăn nằm trong một khoảng giá chỉ định (Từ giá gốc đến giá trần).
     * Phù hợp để triển khai bộ lọc thanh trượt (Price Slider) hoặc các nút phân khúc giá trên UI.
     * * @param minPrice Mức giá tối thiểu (giá sàn).
     * @param maxPrice Mức giá tối đa (giá trần).
     * @return Danh sách các đối tượng {@link MonAn} có giá nằm trong khoảng và đang được bày bán.
     */
    public List<MonAn> getByPriceRange(double minPrice, double maxPrice) {
        List<MonAn> list = new ArrayList<>();
        // Sử dụng toán tử BETWEEN ... AND ... để tối ưu hóa hiệu năng lọc khoảng giá trong SQL
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN WHERE (GIA BETWEEN ? AND ?) AND TRANGTHAI = N'Còn hàng'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Bộ lọc nâng cao: Kết hợp cả Lọc theo loại món VÀ Khoảng giá cùng một lúc.
     * Cực kỳ hữu ích khi người dùng đang ở tab một loại món ăn và muốn lọc giá ngay tại tab đó.
     * * @param idLoai Mã định danh của loại món ăn.
     * @param minPrice Mức giá tối thiểu.
     * @param maxPrice Mức giá tối đa.
     * @return Danh sách các món ăn thỏa mãn đồng thời cả hai điều kiện trên.
     */
    public List<MonAn> getByCategoryAndPrice(int idLoai, double minPrice, double maxPrice) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG "
                + "FROM MONAN WHERE ID_LOAI = ? AND (GIA BETWEEN ? AND ?) AND TRANGTHAI = N'Còn hàng'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLoai);
            ps.setDouble(2, minPrice);
            ps.setDouble(3, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm lọc động nhiều tiêu chí cùng lúc (Multi-criteria Filter).
     * Bất kỳ tham số nào truyền vào là null (hoặc rỗng) sẽ bị bỏ qua, không đưa vào câu lệnh SQL.
     * * @param storeId ID Cửa hàng (truyền null nếu không lọc theo cửa hàng)
     * @param categoryId ID Loại món (truyền null nếu không lọc theo loại)
     * @param minPrice Giá tối thiểu (truyền null nếu không lọc theo giá)
     * @param maxPrice Giá tối đa (truyền null nếu không lọc theo giá)
     * @param keyword Từ khóa tìm kiếm (truyền null hoặc chuỗi rỗng nếu không tìm kiếm)
     * @return Danh sách món ăn thỏa mãn TẤT CẢ các điều kiện được chọn.
     */
    public List<MonAn> filterMonAn(Integer storeId, Integer categoryId, Double minPrice, Double maxPrice, String keyword) {
        List<MonAn> list = new ArrayList<>();

        // Sử dụng StringBuilder để nối chuỗi SQL linh hoạt
        StringBuilder sql = new StringBuilder(
                "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG " +
                        "FROM MONAN WHERE TRANGTHAI = N'Còn hàng'"
        );

        // Danh sách lưu trữ các giá trị tham số dấu hỏi (?) sẽ truyền vào PreparedStatement
        List<Object> params = new ArrayList<>();

        // 1. Kiểm tra lọc theo Cửa hàng
        if (storeId != null) {
            sql.append(" AND ID_CUAHANG = ?");
            params.add(storeId);
        }

        // 2. Kiểm tra lọc theo Loại món
        if (categoryId != null) {
            sql.append(" AND ID_LOAI = ?");
            params.add(categoryId);
        }

        // 3. Kiểm tra lọc theo Khoảng giá
        if (minPrice != null && maxPrice != null) {
            sql.append(" AND (GIA BETWEEN ? AND ?)");
            params.add(minPrice);
            params.add(maxPrice);
        }

        // 4. Kiểm tra lọc theo Tên món
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND TENMON LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Đổ toàn bộ tham số từ List vào PreparedStatement một cách tự động
            for (int i = 0; i < params.size(); i++) {
                // JDBC hỗ trợ setObject để tự động nhận diện kiểu dữ liệu (Int, Double, String)
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonAn(
                            rs.getInt("ID_MONAN"),
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENMON"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("GIA"),
                            rs.getString("IMG")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cửa hàng thêm món ăn mới vào thực đơn.
     * Mặc định khi mới thêm, trạng thái của món ăn sẽ là 'Còn hàng'.
     * * @param storeId ID của cửa hàng thực hiện thêm món (Bắt buộc để gắn đúng chủ).
     * @param idLoai ID danh mục loại món ăn (Ví dụ: Đồ uống, Món chính).
     * @param tenMon Tên món ăn (Hỗ trợ tiếng Việt có dấu).
     * @param gia Giá bán của món ăn.
     * @param img Tên file hoặc đường dẫn ảnh của món ăn.
     * @return true nếu thêm thành công vào cơ sở dữ liệu.
     */
    public boolean insertMonAn(int storeId, int idLoai, String tenMon, double gia, String img) {
        String sql = "INSERT INTO MONAN (ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG) "
                + "VALUES (?, ?, ?, N'Còn hàng', ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            ps.setInt(2, idLoai);
            ps.setNString(3, tenMon);
            ps.setDouble(4, gia);
            ps.setString(5, img);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cửa hàng cập nhật thông tin món ăn (Tên, Giá, Trạng thái, Ảnh).
     * Có đi kèm điều kiện kiểm tra ID_CUAHANG để đảm bảo quán nào chỉ được sửa món của quán đó.
     * 
     * @param monAnId ID của món ăn cần thay đổi.
     * @param storeId ID của cửa hàng (Dùng để xác thực quyền sở hữu món ăn).
     * @param tenMon Tên mới của món ăn.
     * @param gia Giá mới của món ăn.
     * @param trangThai Trạng thái mới (ví dụ: N'Còn hàng', N'het_hang').
     * @param img Đường dẫn ảnh mới.
     * @return true nếu cập nhật thành công.
     */
    public boolean updateMonAn(int monAnId, int storeId, String tenMon, double gia, String trangThai, String img) {
        String sql = "UPDATE MONAN SET TENMON = ?, GIA = ?, TRANGTHAI = ?, IMG = ? "
                + "WHERE ID_MONAN = ? AND ID_CUAHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, tenMon);
            ps.setDouble(2, gia);
            ps.setNString(3, trangThai);
            ps.setString(4, img);
            ps.setInt(5, monAnId);
            ps.setInt(6, storeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa món ăn khỏi thực đơn của cửa hàng.
     * 
     * @param monAnId ID của món ăn cần xóa.
     * @param storeId ID của cửa hàng (Để xác thực quyền sở hữu).
     * @return true nếu xóa thành công.
     * @throws RuntimeException nếu có lỗi hệ thống nghiêm trọng (không phải lỗi ràng buộc dữ liệu).
     */
    public boolean deleteMonAn(int monAnId, int storeId) {
        String sql = "DELETE FROM MONAN WHERE ID_MONAN = ? AND ID_CUAHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, monAnId);
            ps.setInt(2, storeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Lỗi 547 trong SQL Server thường là lỗi vi phạm ràng buộc khóa ngoại (Foreign Key Constraint)
            // Xảy ra khi món ăn này đã có trong đơn hàng/giỏ hàng của ai đó.
            if (e.getErrorCode() == 547) {
                System.err.println("Không thể xóa món ăn ID " + monAnId + " vì nó đã có trong lịch sử đơn hàng.");
            } else {
                e.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}