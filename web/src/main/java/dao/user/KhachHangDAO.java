package dao.user;

import context.DBContext;
import model.user.KhachHang;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác dữ liệu đối với bảng KHACHHANG.
 * Cung cấp các phương thức để đăng ký thông tin khách hàng và truy vấn theo tài khoản.
 */
public class KhachHangDAO {

    /**
     * Đăng ký thông tin chi tiết cho một khách hàng mới.
     *
     * @param kh Đối tượng {@link KhachHang} chứa thông tin cần lưu.
     * @return true nếu lưu thành công, ngược lại trả về false.
     */
    public boolean registerKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kh.getIdTaiKhoan());
            ps.setNString(2, kh.getHoTen());
            ps.setDate(3, kh.getNgaySinh());
            ps.setNString(4, kh.getDiaChi());
            ps.setString(5, kh.getSdt());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm kiếm thông tin khách hàng dựa trên ID tài khoản liên kết.
     *
     * @param accId ID của tài khoản (khóa ngoại từ bảng TAIKHOAN).
     * @return Đối tượng {@link KhachHang} nếu tìm thấy, ngược lại trả về null.
     */
    public KhachHang getByAccountId(int accId) {
        String sql = "SELECT ID_KHACHHANG, ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT FROM KHACHHANG WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accId);

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