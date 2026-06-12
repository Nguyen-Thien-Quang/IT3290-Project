package dao.user;

import context.DBContext;
import model.user.TaiKhoan;
import java.sql.*;

/**
 * Lớp DAO xử lý xác thực tài khoản sử dụng lệnh EXEC gọi Stored Procedure.
 */
public class TaiKhoanDAO {

    /**
     * Xác thực tài khoản đăng nhập thông qua lệnh EXEC check_login.
     * * @param email Địa chỉ email.
     * @param pass Mật khẩu.
     * @return Đối tượng TaiKhoan nếu đúng, ngược lại trả về null.
     */
    public TaiKhoan login(String email, String pass) {
        String sqlProcedure = "EXEC check_login ?, ?";
        String sqlSelect = "SELECT ID_TAIKHOAN, MATKHAU, VAITRO, EMAIL FROM TAIKHOAN WHERE EMAIL = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement psProc = conn.prepareStatement(sqlProcedure)) {

            psProc.setString(1, email);
            psProc.setString(2, pass);

            try (ResultSet rs = psProc.executeQuery()) {
                if (rs.next() && rs.getInt("Result") == 1) {
                    try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                        psSelect.setString(1, email);
                        try (ResultSet rsUser = psSelect.executeQuery()) {
                            if (rsUser.next()) {
                                return new TaiKhoan(
                                        rsUser.getInt("ID_TAIKHOAN"),
                                        rsUser.getString("EMAIL"),
                                        rsUser.getString("MATKHAU"),
                                        rsUser.getString("VAITRO")
                                );
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo tài khoản Khách hàng mới (Sử dụng EXEC create_user).
     */
    public boolean registerKhachHang(String email, String pass, String hoTen, Date ngaySinh, String diaChi, String sdt) {

        // Gọi lại hàm kiểm tra xem email đã tồn tại hay chưa
        if (this.checkEmailExist(email)) {
            System.out.println("Đăng ký thất bại: Email " + email + " đã tồn tại trong hệ thống.");
            return false;
        }

        String sql = "EXEC create_user ?, ?, ?, ?, ?, ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setNString(3, hoTen);
            ps.setDate(4, ngaySinh);
            ps.setNString(5, diaChi);
            ps.setString(6, sdt);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tạo tài khoản Cửa hàng mới (Sử dụng EXEC create_shop_manager).
     */
    public boolean registerCuaHang(String email, String pass, String tenCuaHang, String diaChi, String sdt) {

        // Gọi lại hàm kiểm tra xem email đã tồn tại hay chưa
        if (this.checkEmailExist(email)) {
            System.out.println("Đăng ký thất bại: Email " + email + " đã tồn tại trong hệ thống.");
            return false;
        }

        String sql = "EXEC create_shop_manager ?, ?, ?, ?, ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setNString(3, tenCuaHang);
            ps.setNString(4, diaChi);
            ps.setString(5, sdt);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tạo tài khoản Shipper mới (Sử dụng EXEC sp_create_shipper).
     */
    public boolean registerShipper(String email, String pass, String hoTen, Date ngaySinh, String sdt) {

        // Gọi lại hàm kiểm tra xem email đã tồn tại hay chưa
        if (this.checkEmailExist(email)) {
            System.out.println("Đăng ký thất bại: Email " + email + " đã tồn tại trong hệ thống.");
            return false;
        }

        String sql = "EXEC sp_create_shipper ?, ?, ?, ?, ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setNString(3, hoTen);
            ps.setDate(4, ngaySinh);
            ps.setString(5, sdt);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy trực tiếp chuỗi Email từ bảng TAIKHOAN thông qua ID_TAIKHOAN.
     * Giải pháp này giúp lấy thông tin tài khoản mà không cần chỉnh sửa các lớp Model vai trò.
     * * @param idTaiKhoan ID duy nhất của tài khoản cần tra cứu.
     * @return Chuỗi email nếu tìm thấy, ngược lại trả về null.
     */
    public String getEmailByTaiKhoanId(int idTaiKhoan) {
        String sql = "SELECT EMAIL FROM TAIKHOAN WHERE ID_TAIKHOAN = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTaiKhoan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EMAIL");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra xem một Email đã được đăng ký trong hệ thống hay chưa.
     * Dùng để chặn trùng lặp dữ liệu khi người dùng đăng ký tài khoản mới.
     * * @param email Chuỗi email cần kiểm tra.
     * @return true nếu email đã tồn tại trong bảng TAIKHOAN, ngược lại trả về false.
     */
    public boolean checkEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM TAIKHOAN WHERE EMAIL = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Nếu số lượng đếm được > 0 nghĩa là email đã tồn tại
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}