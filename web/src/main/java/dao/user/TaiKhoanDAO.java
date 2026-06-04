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