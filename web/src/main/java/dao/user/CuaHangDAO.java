package dao.user;

import context.DBContext;
import model.user.CuaHang;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác dữ liệu đối với bảng CUAHANG.
 * Quản lý thông tin doanh nghiệp, địa chỉ và cập nhật doanh thu của cửa hàng.
 */
public class CuaHangDAO {

    /**
     * Đăng ký thông tin chi tiết cho một cửa hàng mới.
     * Mặc định doanh thu khởi tạo sẽ là 0.
     *
     * @param ch Đối tượng {@link CuaHang} chứa thông tin cửa hàng cần lưu.
     * @return true nếu đăng ký thành công, ngược lại trả về false.
     */
    public boolean registerCuaHang(CuaHang ch) {
        String sql = "INSERT INTO CUAHANG (ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ch.getIdTaiKhoan());
            ps.setNString(2, ch.getTenCuaHang());
            ps.setNString(3, ch.getDiaChi());
            ps.setString(4, ch.getSdt());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm kiếm thông tin cửa hàng dựa trên ID tài khoản liên kết.
     *
     * @param accId ID tài khoản của chủ cửa hàng (khóa ngoại từ bảng TAIKHOAN).
     * @return Đối tượng {@link CuaHang} nếu tìm thấy, ngược lại trả về null.
     */
    public CuaHang getByAccountId(int accId) {
        String sql = "SELECT ID_CUAHANG, ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU FROM CUAHANG WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accId);

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
     * Cập nhật doanh thu tích lũy cho cửa hàng sau mỗi đơn hàng thành công.
     *
     * @param idCuaHang ID của cửa hàng cần cập nhật.
     * @param amount Số tiền cộng thêm vào doanh thu hiện tại.
     */
    public void updateRevenue(int idCuaHang, double amount) {
        String sql = "UPDATE CUAHANG SET DOANHTHU = DOANHTHU + ? WHERE ID_CUAHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, idCuaHang);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}