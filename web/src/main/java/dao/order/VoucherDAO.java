package dao.order;

import context.DBContext;
import model.order.Voucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO quản lý truy xuất kho mã giảm giá của khách hàng.
 */
public class VoucherDAO {

    /**
     * Lấy danh sách toàn bộ Voucher mà một khách hàng đang sở hữu.
     * Dùng để hiển thị lên màn hình lúc thanh toán.
     * @param khId ID của khách hàng.
     * @return Danh sách các Voucher thuộc về khách hàng đó.
     */
    public List<Voucher> getByKhachHang(int khId) {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT ID_VOUCHER, ID_KHACHHANG, LOAIVOUCHER FROM VOUCHER WHERE ID_KHACHHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, khId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Voucher(
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getNString("LOAIVOUCHER")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}