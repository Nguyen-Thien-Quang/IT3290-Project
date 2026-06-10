package dao.order;

import context.DBContext;
import model.order.DonHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO quản lý đơn hàng.
 * Hỗ trợ toàn bộ luồng nghiệp vụ: Đặt hàng (có Voucher) -> Chờ giao -> Đang giao -> Đã giao.
 */
public class DonHangDAO {

    /**
     * Tiến hành đặt và tạo đơn hàng từ giỏ hàng (Sử dụng EXEC checkout_cart).
     * @param ghId ID giỏ hàng cần đặt.
     * @param khId ID khách hàng đặt đơn.
     * @param phuongThuc Phương thức thanh toán (Tiền mặt, Chuyển khoản,...).
     * @param voucherId ID Voucher khách sử dụng (Truyền null nếu khách không dùng mã).
     * @return true nếu checkout thành công.
     */
    public boolean makeDonHang(int ghId, int khId, String phuongThuc, Integer voucherId) {
        // Cập nhật procedure để nhận 4 tham số
        String sql = "EXEC checkout_cart ?, ?, ?, ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ghId);
            ps.setInt(2, khId);
            ps.setNString(3, phuongThuc);

            // Xử lý an toàn trường hợp khách không áp dụng mã giảm giá (voucherId = null)
            if (voucherId != null) {
                ps.setInt(4, voucherId);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy lịch sử mua hàng của một Khách hàng cụ thể.
     * @param khId ID của khách hàng.
     * @return Danh sách các đơn hàng đã đặt, sắp xếp theo thời gian mới nhất.
     */
    public List<DonHang> getHistoryByKhachHang(int khId) {
        List<DonHang> list = new ArrayList<>();
        // Bổ sung ID_VOUCHER vào câu truy vấn SELECT
        String sql = "SELECT ID_DONHANG, ID_GIOHANG, ID_VOUCHER, ID_KHACHHANG, ID_SHIPPER, THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG WHERE ID_KHACHHANG = ? ORDER BY THOIGIANDAT DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, khId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHang(
                            rs.getInt("ID_DONHANG"),
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_VOUCHER"), // Lấy dữ liệu Voucher từ DB
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_SHIPPER"),
                            rs.getTimestamp("THOIGIANDAT"),
                            rs.getNString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN"),
                            rs.getNString("PHUONGTHUCTHANHTOAN")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách các đơn hàng mới đang chờ được giao.
     * Hiển thị trên màn hình của Shipper để họ chọn đơn.
     * @return Danh sách các đơn hàng có trạng thái 'Chờ xác nhận'.
     */
    public List<DonHang> getPendingOrdersForShipper() {
        List<DonHang> list = new ArrayList<>();
        // Bổ sung ID_VOUCHER vào câu truy vấn SELECT
        String sql = "SELECT ID_DONHANG, ID_GIOHANG, ID_VOUCHER, ID_KHACHHANG, ID_SHIPPER, THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG WHERE TRANGTHAI = N'Chờ xác nhận' ORDER BY THOIGIANDAT ASC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DonHang(
                        rs.getInt("ID_DONHANG"),
                        rs.getInt("ID_GIOHANG"),
                        rs.getInt("ID_VOUCHER"), // Lấy dữ liệu Voucher từ DB
                        rs.getInt("ID_KHACHHANG"),
                        rs.getInt("ID_SHIPPER"),
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
     * Shipper bấm nhận đơn hàng.
     * Hệ thống sẽ gán ID của Shipper vào đơn và đổi trạng thái thành 'Đang giao'.
     * Điều kiện `TRANGTHAI = N'Chờ xác nhận'` để đảm bảo không có 2 shipper cùng nhận 1 đơn.
     * @param donHangId ID của đơn hàng.
     * @param shipperId ID của Shipper nhận giao đơn này.
     * @return true nếu nhận đơn thành công.
     */
    public boolean acceptOrder(int donHangId, int shipperId) {
        String sql = "UPDATE DONHANG SET ID_SHIPPER = ?, TRANGTHAI = N'Đang giao' "
                + "WHERE ID_DONHANG = ? AND TRANGTHAI = N'Chờ xác nhận'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipperId);
            ps.setInt(2, donHangId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shipper xác nhận đã giao hàng thành công tới tay khách.
     * Đổi trạng thái đơn hàng thành 'Đã giao'.
     * Ngay lúc này, dữ liệu doanh thu của cửa hàng (dựa trên trạng thái 'Đã giao') sẽ chính thức được chốt.
     * @param donHangId ID của đơn hàng đã giao xong.
     * @return true nếu cập nhật thành công.
     */
    public boolean confirmDelivery(int donHangId) {
        String sql = "UPDATE DONHANG SET TRANGTHAI = N'Đã giao' WHERE ID_DONHANG = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donHangId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách toàn bộ đơn hàng có chứa món ăn của một Cửa hàng cụ thể.
     * Sắp xếp theo thời gian đặt mới nhất để chủ quán tiện theo dõi.
     * @param storeId ID của cửa hàng cần xem đơn.
     * @return Danh sách các đối tượng {@link DonHang} tương ứng.
     */
    public List<DonHang> getOrdersByStore(int storeId) {
        List<DonHang> list = new ArrayList<>();
        // Sử dụng DISTINCT để tránh trùng lặp đơn hàng khi một đơn có nhiều món của cùng 1 quán
        String sql = "SELECT DISTINCT D.ID_DONHANG, D.ID_GIOHANG, D.ID_VOUCHER, D.ID_KHACHHANG, D.ID_SHIPPER, D.THOIGIANDAT, D.TRANGTHAI, D.TONGTIEN, D.PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG D "
                + "JOIN GIOHANG_MONAN GM ON D.ID_GIOHANG = GM.ID_GIOHANG "
                + "JOIN MONAN M ON GM.ID_MONAN = M.ID_MONAN "
                + "WHERE M.ID_CUAHANG = ? "
                + "ORDER BY D.THOIGIANDAT DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHang(
                            rs.getInt("ID_DONHANG"),
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_SHIPPER"),
                            rs.getTimestamp("THOIGIANDAT"),
                            rs.getNString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN"),
                            rs.getNString("PHUONGTHUCTHANHTOAN")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lọc đơn hàng của cửa hàng theo Trạng thái cụ thể.
     * Rất hữu ích khi chia tab trên giao diện Admin của quán (Tab Chờ ship, Tab Đang giao, Tab Lịch sử đã giao).
     * @param storeId ID của cửa hàng.
     * @param status Trạng thái đơn hàng cần lọc (N'Chờ xác nhận', N'Đang giao', N'Đã giao').
     * @return Danh sách đơn hàng thỏa mãn điều kiện.
     */
    public List<DonHang> getOrdersByStoreAndStatus(int storeId, String status) {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT DISTINCT D.ID_DONHANG, D.ID_GIOHANG, D.ID_VOUCHER, D.ID_KHACHHANG, D.ID_SHIPPER, D.THOIGIANDAT, D.TRANGTHAI, D.TONGTIEN, D.PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG D "
                + "JOIN GIOHANG_MONAN GM ON D.ID_GIOHANG = GM.ID_GIOHANG "
                + "JOIN MONAN M ON GM.ID_MONAN = M.ID_MONAN "
                + "WHERE M.ID_CUAHANG = ? AND D.TRANGTHAI = ? "
                + "ORDER BY D.THOIGIANDAT DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            ps.setNString(2, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHang(
                            rs.getInt("ID_DONHANG"),
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_SHIPPER"),
                            rs.getTimestamp("THOIGIANDAT"),
                            rs.getNString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN"),
                            rs.getNString("PHUONGTHUCTHANHTOAN")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách lịch sử các đơn hàng mà một Shipper đã nhận giao.
     * Danh sách này bao gồm cả đơn 'Đang giao' và 'Đã giao', sắp xếp theo thời gian mới nhất.
     * @param shipperId ID của Shipper cần xem lịch sử.
     * @return Danh sách các đối tượng {@link DonHang}.
     */
    public List<DonHang> getDonHangsByShipper(int shipperId) {
        List<DonHang> list = new ArrayList<>();
        // Truy vấn tất cả đơn hàng có gắn ID của Shipper này
        String sql = "SELECT ID_DONHANG, ID_GIOHANG, ID_VOUCHER, ID_KHACHHANG, ID_SHIPPER, "
                + "THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG "
                + "WHERE ID_SHIPPER = ? "
                + "ORDER BY THOIGIANDAT DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipperId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHang(
                            rs.getInt("ID_DONHANG"),
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_SHIPPER"),
                            rs.getTimestamp("THOIGIANDAT"),
                            rs.getNString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN"),
                            rs.getNString("PHUONGTHUCTHANHTOAN")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách các đơn hàng mà một Shipper đang đi giao (Trạng thái 'Đang giao').
     * @param shipperId ID của Shipper.
     * @return Danh sách các đơn hàng đang được giao bởi shipper này.
     */
    public List<DonHang> getShippingOrdersByShipper(int shipperId) {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT ID_DONHANG, ID_GIOHANG, ID_VOUCHER, ID_KHACHHANG, ID_SHIPPER, "
                + "THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN "
                + "FROM DONHANG "
                + "WHERE ID_SHIPPER = ? AND TRANGTHAI = N'Đang giao' "
                + "ORDER BY THOIGIANDAT DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipperId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHang(
                            rs.getInt("ID_DONHANG"),
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_VOUCHER"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getInt("ID_SHIPPER"),
                            rs.getTimestamp("THOIGIANDAT"),
                            rs.getNString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN"),
                            rs.getNString("PHUONGTHUCTHANHTOAN")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}