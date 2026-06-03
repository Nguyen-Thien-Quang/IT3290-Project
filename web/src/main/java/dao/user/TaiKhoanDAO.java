package dao.user;

import context.DBContext;
import model.user.TaiKhoan;
import java.sql.*;

/**
 * Lớp DAO xử lý các thao tác liên quan đến bảng TAIKHOAN trong cơ sở dữ liệu.
 * Bao gồm các chức năng: Đăng nhập, kiểm tra sự tồn tại và tạo tài khoản mới.
 */
public class TaiKhoanDAO {

    /**
     * Thực hiện kiểm tra thông tin đăng nhập của người dùng.
     *
     * @param email Địa chỉ email dùng để đăng nhập.
     * @param pass Mật khẩu của tài khoản.
     * @return Đối tượng {@link TaiKhoan} nếu thông tin chính xác, ngược lại trả về null.
     */
    public TaiKhoan login(String email, String pass) {
        String query = "SELECT ID_TAIKHOAN, EMAIL, MATKHAU, VAITRO FROM TAIKHOAN WHERE EMAIL = ? AND MATKHAU = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoan(
                            rs.getInt("ID_TAIKHOAN"),
                            rs.getString("EMAIL"),
                            rs.getString("MATKHAU"),
                            rs.getString("VAITRO")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra xem một email đã tồn tại trong hệ thống hay chưa.
     * Thường dùng trong chức năng Đăng ký để tránh trùng lặp tài khoản.
     *
     * @param email Email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa.
     */
    public boolean checkEmailExist(String email) {
        String query = "SELECT ID_TAIKHOAN FROM TAIKHOAN WHERE EMAIL = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tạo một tài khoản người dùng mới vào cơ sở dữ liệu.
     *
     * @param email Email đăng ký.
     * @param pass Mật khẩu đăng ký.
     * @param role Vai trò của tài khoản (KHACHHANG, CUAHANG, SHIPPER).
     * @return ID của tài khoản vừa tạo nếu thành công, ngược lại trả về -1.
     */
    public int createAccount(String email, String pass, String role) {
        String query = "INSERT INTO TAIKHOAN (EMAIL, MATKHAU, VAITRO) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setString(3, role);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt("ID_TAIKHOAN");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    // đăng kí tài khoản Khách hàng sử dụng stored procedure create_user
    public int createCustomerAccount(String email, String matKhau, String hoTen, String ngaysinh, String diachi, String sdt) {
        String query = "exec create_user ?, ?, ?, ?, ?, ?";
        try(Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, matKhau);
            ps.setString(3, hoTen);
            ps.setString(4, ngaysinh);
            ps.setString(5, diachi);
            ps.setString(6, sdt);

            return ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}