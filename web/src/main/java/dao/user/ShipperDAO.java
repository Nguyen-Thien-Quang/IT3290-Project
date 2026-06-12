package dao.user;

import context.DBContext;
import model.user.Shipper;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến Shipper.
 */
public class ShipperDAO {

    /**
     * Truy vấn thông tin hồ sơ của Shipper dựa trên ID tài khoản.
     * @param accountId ID của tài khoản liên kết (ID_TAIKHOAN).
     * @return Đối tượng {@link Shipper} hoặc null nếu không tìm thấy.
     */
    public Shipper getShipperByAccountId(int accountId) {
        String sql = "SELECT ID_SHIPPER, ID_TAIKHOAN, HOTEN, NGAYSINH, SDT "
                + "FROM SHIPPER WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);

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

    /**
     * Truy vấn thông tin hồ sơ của Shipper dựa trên ID.
     * Phục vụ cho trang cá nhân của tài xế trên ứng dụng nhận đơn.
     * @param shipperId ID duy nhất của Shipper.
     * @return Đối tượng {@link Shipper} chứa thông tin chi tiết, hoặc null nếu không tìm thấy.
     */
    public Shipper getShipperProfileById(int shipperId) {
        String sql = "SELECT ID_SHIPPER, ID_TAIKHOAN, HOTEN, NGAYSINH, SDT "
                + "FROM SHIPPER WHERE ID_SHIPPER = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipperId);

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