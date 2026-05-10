package dao.order;

import context.DBContext;
import model.order.GioHang;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác dữ liệu đối với bảng GIOHANG và GIOHANG_MONAN.
 * Quản lý vòng đời giỏ hàng từ khi tạo mới đến khi thêm sản phẩm.
 */
public class GioHangDAO {

    /**
     * Tạo một giỏ hàng mới cho khách hàng với trạng thái mặc định là 'dang_tao'.
     *
     * @param khId ID của khách hàng sở hữu giỏ hàng.
     * @return ID của giỏ hàng vừa tạo, hoặc -1 nếu thất bại.
     */
    public int createCart(int khId) {
        String sql = "INSERT INTO GIOHANG (ID_KHACHHANG, TRANGTHAI, TONGTIEN) VALUES (?, 'dang_tao', 0)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, khId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Truy vấn giỏ hàng hiện đang ở trạng thái 'dang_tao' của khách hàng.
     *
     * @param khId ID của khách hàng.
     * @return Đối tượng {@link GioHang} nếu tìm thấy, ngược lại trả về null.
     */
    public GioHang getActiveCart(int khId) {
        String sql = "SELECT TOP 1 ID_GIOHANG, ID_KHACHHANG, TRANGTHAI, TONGTIEN "
                + "FROM GIOHANG WHERE ID_KHACHHANG = ? AND TRANGTHAI = 'dang_tao'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, khId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GioHang(
                            rs.getInt("ID_GIOHANG"),
                            rs.getInt("ID_KHACHHANG"),
                            rs.getString("TRANGTHAI"),
                            rs.getDouble("TONGTIEN")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm món ăn vào giỏ hàng. Nếu món ăn đã tồn tại sẽ cập nhật số lượng,
     * nếu chưa sẽ tạo bản ghi mới trong bảng GIOHANG_MONAN.
     *
     * @param ghId ID của giỏ hàng.
     * @param monId ID của món ăn cần thêm.
     * @param qty Số lượng món ăn thêm vào.
     */
    public void addItemToCart(int ghId, int monId, int qty) {
        // Sử dụng câu lệnh lồng nhau để tối ưu hóa việc kiểm tra tồn tại bản ghi
        String sql = "IF EXISTS (SELECT 1 FROM GIOHANG_MONAN WHERE ID_GIOHANG = ? AND ID_MONAN = ?) "
                + "UPDATE GIOHANG_MONAN SET SOLUONG = SOLUONG + ? WHERE ID_GIOHANG = ? AND ID_MONAN = ? "
                + "ELSE INSERT INTO GIOHANG_MONAN (ID_GIOHANG, ID_MONAN, SOLUONG) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Tham số cho phần IF EXISTS
            ps.setInt(1, ghId);
            ps.setInt(2, monId);

            // Tham số cho phần UPDATE
            ps.setInt(3, qty);
            ps.setInt(4, ghId);
            ps.setInt(5, monId);

            // Tham số cho phần INSERT
            ps.setInt(6, ghId);
            ps.setInt(7, monId);
            ps.setInt(8, qty);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}