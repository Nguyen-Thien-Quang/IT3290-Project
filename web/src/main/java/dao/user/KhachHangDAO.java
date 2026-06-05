package dao.user;

import context.DBContext;
import model.user.KhachHang;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến thông tin Khách hàng.
 */
public class KhachHangDAO {

    /**
     * Truy vấn toàn bộ thông tin cá nhân của một khách hàng dựa trên ID.
     * Dùng để đổ dữ liệu lên trang quản lý thông tin cá nhân (Profile).
     * * @param khId ID duy nhất của khách hàng cần xem thông tin.
     * @return Đối tượng {@link KhachHang} chứa đầy đủ thông tin, hoặc null nếu không tìm thấy.
     */
    public KhachHang getProfileById(int khId) {
        String sql = "SELECT ID_KHACHHANG, ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT "
                + "FROM KHACHHANG WHERE ID_KHACHHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, khId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getNString("HOTEN"),
                            rs.getDate("NGAYSINH"),
                            rs.getNString("DIACHI"),
                            rs.getString("SDT")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}