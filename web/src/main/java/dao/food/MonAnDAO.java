package dao.food;

import context.DBContext;
import model.food.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác dữ liệu đối với bảng MONAN.
 * Quản lý danh sách món ăn, lọc theo cửa hàng và các thao tác thêm, xóa món.
 */
public class MonAnDAO {

    /**
     * Thêm một món ăn mới vào cơ sở dữ liệu.
     *
     * @param m Đối tượng {@link MonAn} chứa thông tin món ăn cần thêm.
     */
    public void insert(MonAn m) {
        String sql = "INSERT INTO MONAN (ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getIdCuaHang());
            ps.setInt(2, m.getIdLoai());
            ps.setNString(3, m.getTenMon());
            ps.setString(4, m.getTrangThai());
            ps.setDouble(5, m.getGia());
            ps.setString(6, m.getImg());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách tất cả các món ăn đang ở trạng thái 'con_ban'.
     *
     * @return Danh sách các đối tượng {@link MonAn}.
     */
    public List<MonAn> getAll() {
        List<MonAn> list = new ArrayList<>();
        // Sử dụng tên cột cụ thể thay vì dấu * để tăng tốc độ truy vấn
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG FROM MONAN WHERE TRANGTHAI = 'con_ban'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách món ăn thuộc về một cửa hàng cụ thể.
     *
     * @param storeId ID của cửa hàng cần lấy thực đơn.
     * @return Danh sách các món ăn của cửa hàng đó.
     */
    public List<MonAn> getByStore(int storeId) {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT ID_MONAN, ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG FROM MONAN WHERE ID_CUAHANG = ?";

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
     * Xóa một món ăn khỏi hệ thống dựa trên ID.
     * @param id ID của món ăn cần xóa.
     */
    public void delete(int id) {
        String sql = "DELETE FROM MONAN WHERE ID_MONAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}