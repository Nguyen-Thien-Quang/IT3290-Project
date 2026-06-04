package dao.order;

import context.DBContext;
import model.order.GioHang;
import java.sql.*;

/**
 * Lớp DAO (Data Access Object) xử lý các thao tác dữ liệu liên quan đến Giỏ hàng.
 * Tận dụng các tính năng tối ưu hóa của SQL Server và Trigger tự động để tính toán dữ liệu tài chính.
 * * @author Nguyễn Tiến Quân
 */
public class GioHangDAO {

    /**
     * Khởi tạo một giỏ hàng mới cho khách hàng với trạng thái ban đầu là 'Đang tạo'.
     * Tổng tiền ban đầu mặc định được thiết lập bằng 0.
     * * @param khId Mã định danh duy nhất (ID) của khách hàng cần tạo giỏ hàng.
     * @return ID của giỏ hàng vừa được sinh tự động trong Database,
     * hoặc -1 nếu quá trình khởi tạo thất bại.
     */
    public int createCart(int khId) {
        // Đồng bộ chuỗi trạng thái có dấu N'Đang tạo' khớp với logic của Stored Procedure checkout
        String sql = "INSERT INTO GIOHANG (ID_KHACHHANG, TRANGTHAI, TONGTIEN) VALUES (?, N'Đang tạo', 0)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, khId);
            ps.executeUpdate();

            // Lấy lại ID tự động tăng (Identity) vừa được sinh ra từ câu lệnh INSERT
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
     * Truy vấn giỏ hàng hiện tại đang hoạt động (ở trạng thái 'Đang tạo') của khách hàng.
     * Hàm này giúp xác định khách hàng có giỏ hàng cũ chưa thanh toán hay không trước khi tạo giỏ mới.
     * * @param khId Mã định danh duy nhất (ID) của khách hàng cần kiểm tra.
     * @return Đối tượng {@link GioHang} chứa thông tin giỏ hàng hiện tại nếu tìm thấy,
     * ngược lại trả về null nếu khách hàng chưa có giỏ hàng hoạt động.
     */
    public GioHang getActiveCart(int khId) {
        // Sử dụng TOP 1 để tối ưu hiệu năng truy vấn, luôn lấy bản ghi mới nhất đang tạo
        String sql = "SELECT TOP 1 ID_GIOHANG, ID_KHACHHANG, TRANGTHAI, TONGTIEN "
                + "FROM GIOHANG WHERE ID_KHACHHANG = ? AND TRANGTHAI = N'Đang tạo'";

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
     * Thêm một món ăn vào giỏ hàng chỉ định hoặc cập nhật số lượng nếu món ăn đã tồn tại.
     * Logic kiểm tra trùng lặp và tính toán tổng tiền được tối ưu hóa tối đa:
     * - Sử dụng khối lệnh IF EXISTS của SQL Server để tránh phải gọi 2 câu lệnh độc lập từ Java.
     * - Kích hoạt ngầm Trigger TRG_CAPNHAT_TONGTIEN_GIOHANG trên cơ sở dữ liệu để tự động
     * tính lại tổng tiền (Số lượng * Giá món) của bảng GIOHANG mà không cần code Java can thiệp.
     * * @param ghId Mã định danh duy nhất (ID) của giỏ hàng hiện tại cần thêm món.
     * @param monId Mã định danh duy nhất (ID) của món ăn cần thêm vào giỏ.
     * @param qty Số lượng món ăn muốn thêm hoặc muốn cộng dồn vào giỏ.
     */
    public void addItemToCart(int ghId, int monId, int qty) {
        // Cú pháp gộp kiểm tra tồn tại bản ghi trung gian để tăng hiệu năng xử lý
        String sql = "IF EXISTS (SELECT 1 FROM GIOHANG_MONAN WHERE ID_GIOHANG = ? AND ID_MONAN = ?) "
                + "UPDATE GIOHANG_MONAN SET SOLUONG = SOLUONG + ? WHERE ID_GIOHANG = ? AND ID_MONAN = ? "
                + "ELSE INSERT INTO GIOHANG_MONAN (ID_GIOHANG, ID_MONAN, SOLUONG) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cho biểu thức kiểm tra điều kiện IF EXISTS
            ps.setInt(1, ghId);
            ps.setInt(2, monId);

            // Thiết lập các tham số cho khối lệnh UPDATE (nếu món ăn đã có sẵn trong giỏ)
            ps.setInt(3, qty);
            ps.setInt(4, ghId);
            ps.setInt(5, monId);

            // Thiết lập các tham số cho khối lệnh INSERT (nếu món ăn chưa từng có trong giỏ)
            ps.setInt(6, ghId);
            ps.setInt(7, monId);
            ps.setInt(8, qty);

            // Thực thi cập nhật cấu trúc giỏ hàng.
            // Database sẽ tự động kích hoạt Trigger tính tổng tiền ngay sau dòng này.
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}