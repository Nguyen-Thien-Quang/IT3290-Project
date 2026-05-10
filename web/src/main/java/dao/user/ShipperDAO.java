package dao.user;

import context.DBContext;
import model.user.Shipper;
import java.sql.*;

/**
 * Lớp DAO chịu trách nhiệm thực hiện các thao tác dữ liệu với bảng SHIPPER.
 * Bao gồm việc lưu trữ thông tin cá nhân của người giao hàng sau khi tạo tài khoản.
 */
public class ShipperDAO {

    /**
     * Đăng ký thông tin chi tiết cho một nhân viên giao hàng (Shipper) mới.
     *
     * @param s Đối tượng {@link Shipper} chứa các thông tin cần lưu trữ.
     * @return true nếu thông tin được lưu thành công, ngược lại trả về false.
     */
    public boolean registerShipper(Shipper s) {
        String sql = "INSERT INTO SHIPPER (ID_TAIKHOAN, HOTEN, NGAYSINH, SDT) VALUES (?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getIdTaiKhoan());
            ps.setNString(2, s.getHoTen());
            ps.setDate(3, s.getNgaySinh());
            ps.setString(4, s.getSdt());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy thông tin Shipper dựa trên ID tài khoản liên kết.
     *
     * @param accId ID tài khoản của Shipper (khóa ngoại từ bảng TAIKHOAN).
     * @return Đối tượng {@link Shipper} nếu tìm thấy, ngược lại trả về null.
     */
    public Shipper getByAccountId(int accId) {
        String sql = "SELECT ID_SHIPPER, ID_TAIKHOAN, HOTEN, NGAYSINH, SDT FROM SHIPPER WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Shipper(
                            rs.getInt("ID_SHIPPER"),
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getNString("HOTEN"),
                            rs.getDate("NGAYSINH"),
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