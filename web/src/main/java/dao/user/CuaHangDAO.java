package dao.user;

import context.DBContext;
import model.user.CuaHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến Cửa hàng (Merchant).
 */
public class CuaHangDAO {

    /**
     * Truy vấn thông tin Cửa hàng dựa trên ID tài khoản.
     * @param accountId ID của tài khoản liên kết (ID_TAIKHOAN).
     * @return Đối tượng {@link CuaHang} hoặc null nếu không tìm thấy.
     */
    public CuaHang getCuaHangByAccountId(int accountId) {
        String sql = "SELECT ID_CUAHANG, ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU "
                + "FROM CUAHANG WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CuaHang(
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getNString("TENCUAHANG"),
                            rs.getNString("DIACHI"),
                            rs.getString("SDT"),
                            rs.getDouble("DOANHTHU")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Truy vấn thông tin chi tiết của một cửa hàng dựa trên ID.
     * Phục vụ hiển thị trên trang Dashboard quản lý của chủ quán.
     * @param storeId ID duy nhất của cửa hàng.
     * @return Đối tượng {@link CuaHang} chứa thông tin chi tiết, hoặc null nếu không tìm thấy.
     */
    public CuaHang getStoreProfileById(int storeId) {
        String sql = "SELECT ID_CUAHANG, ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU "
                + "FROM CUAHANG WHERE ID_CUAHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CuaHang(
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getNString("TENCUAHANG"),
                            rs.getNString("DIACHI"),
                            rs.getString("SDT"),
                            rs.getDouble("DOANHTHU")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm kiếm danh sách cửa hàng dựa trên từ khóa (Tên cửa hàng).
     * Hỗ trợ tìm kiếm tương đối (chứa từ khóa) và tiếng Việt có dấu.
     * * @param keyword Từ khóa người dùng nhập vào ô tìm kiếm.
     * @return Danh sách các đối tượng {@link CuaHang} khớp với từ khóa.
     */
    public List<CuaHang> searchCuaHangByKeyword(String keyword) {
        List<CuaHang> list = new ArrayList<>();
        // Sử dụng LIKE để tìm kiếm chuỗi con
        String sql = "SELECT ID_CUAHANG, ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU "
                + "FROM CUAHANG WHERE TENCUAHANG LIKE ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Bao bọc từ khóa bằng ký tự % để tìm kiếm tương đối ở cả 2 đầu
            // Ví dụ nhập "Mixue" -> truy vấn sẽ là "%Mixue%"
            ps.setNString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CuaHang(
                            rs.getInt("ID_CUAHANG"),
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getNString("TENCUAHANG"),
                            rs.getNString("DIACHI"),
                            rs.getString("SDT"),
                            rs.getDouble("DOANHTHU")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}