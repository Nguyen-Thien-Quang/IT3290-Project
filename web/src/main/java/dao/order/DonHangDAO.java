package dao.order;

import context.DBContext;
import model.order.DonHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO chịu trách nhiệm quản lý dữ liệu cho bảng DONHANG.
 * Xử lý quy trình tạo đơn hàng, truy vấn đơn hàng chờ xác nhận và cập nhật trạng thái đơn hàng.
 */
public class DonHangDAO {

    /**
     * Tạo đơn hàng mới từ giỏ hàng và cập nhật trạng thái giỏ hàng thành 'da_dat'.
     *
     * @param d Đối tượng {@link DonHang} chứa thông tin đơn hàng cần tạo
     */
    public void createOrder(DonHang d) {
        String sqlOrder = "INSERT INTO DONHANG (ID_GIOHANG, ID_KHACHHANG, THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN) VALUES (?, ?, GETDATE(), N'cho_xac_nhan', ?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            // Tắt auto-commit để đảm bảo tính toàn vẹn (Transaction) giữa tạo đơn và cập nhật giỏ
            conn.setAutoCommit(false);

            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {
                psOrder.setInt(1, d.getIdGioHang());
                psOrder.setInt(2, d.getIdKhachHang());
                psOrder.setDouble(3, d.getTongTien());
                psOrder.setNString(4, d.getPhuongThucThanhToan());
                psOrder.executeUpdate();

                // Gọi hàm hỗ trợ cập nhật trạng thái giỏ hàng nội bộ
                updateCartStatus(conn, d.getIdGioHang());

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách các đơn hàng mới đang chờ cửa hàng hoặc quản trị viên xác nhận.
     *
     * @return Danh sách các đối tượng {@link DonHang} có trạng thái 'cho_xac_nhan'.
     */
    public List<DonHang> getPendingOrders() {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT ID_DONHANG, ID_GIOHANG, ID_KHACHHANG, ID_SHIPPER, ID_VOUCHER, "
                + "THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG WHERE TRANGTHAI = N'cho_xac_nhan'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DonHang(
                        rs.getInt("ID_DONHANG"),
                        rs.getInt("ID_GIOHANG"),
                        rs.getInt("ID_KHACHHANG"),
                        rs.getObject("ID_SHIPPER", Integer.class), // Xử lý an toàn cho trường có thể NULL
                        rs.getObject("ID_VOUCHER", Integer.class), // Xử lý an toàn cho trường có thể NULL
                        rs.getTimestamp("THOIGIANDAT"),
                        rs.getNString("TRANGTHAI"),
                        rs.getDouble("TONGTIEN"),
                        rs.getNString("PHUONGTHUCTHANHTOAN")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật trạng thái đơn hàng và gán nhân viên giao hàng (Shipper)
     *
     * @param orderId ID của đơn hàng cần cập nhật
     * @param shipperId ID của shipper tiếp nhận đơn
     * @param status Trạng thái mới của đơn hàng (dang_giao, da_giao, da_huy)
     */
    public void updateStatus(int orderId, int shipperId, String status) {
        String sql = "UPDATE DONHANG SET ID_SHIPPER = ?, TRANGTHAI = ? WHERE ID_DONHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipperId);
            ps.setNString(2, status);
            ps.setInt(3, orderId);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức hỗ trợ nội bộ để cập nhật trạng thái giỏ hàng sau khi đặt thành công.
     */
    private void updateCartStatus(Connection conn, int ghId) throws SQLException {
        String sql = "UPDATE GIOHANG SET TRANGTHAI = 'da_dat' WHERE ID_GIOHANG = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ghId);
            ps.executeUpdate();
        }
    }
}