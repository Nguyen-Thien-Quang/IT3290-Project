package dao.order;

import context.DBContext;
import model.order.Voucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác liên quan đến bảng VOUCHER.
 * Hỗ trợ truy vấn theo khách hàng và lọc theo loại ưu đãi.
 */
public class VoucherDAO {

    /**
     * Lấy danh sách voucher của một khách hàng cụ thể.
     *
     * @param customerId ID của khách hàng cần kiểm tra ví voucher.
     * @return Danh sách các đối tượng {@link Voucher}.
     */
    public List<Voucher> getByCustomer(int customerId) {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT ID_VOUCHER, ID_KHACHHANG, LOAIVOUCHER FROM VOUCHER WHERE ID_KHACHHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
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

    /**
     * Truy vấn tìm kiếm voucher theo loại (ví dụ: 'giam_100k', 'freeship').
     *
     * @param type Chuỗi mô tả loại voucher cần tìm kiếm.
     * @return Danh sách các voucher khớp với mô tả.
     */
    public List<Voucher> getByType(String type) {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT ID_VOUCHER, ID_KHACHHANG, LOAIVOUCHER FROM VOUCHER WHERE LOAIVOUCHER LIKE ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, "%" + type + "%");

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

    /**
     * Lấy thông tin chi tiết của một voucher dựa trên ID.
     * Dùng để kiểm tra logic tính giảm giá khi khách hàng áp dụng mã vào đơn hàng.
     */
    public Voucher getById(int voucherId) {
        String sql = "SELECT ID_VOUCHER, ID_KHACHHANG, LOAIVOUCHER FROM VOUCHER WHERE ID_VOUCHER = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, voucherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Voucher(
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getNString("LOAIVOUCHER")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}