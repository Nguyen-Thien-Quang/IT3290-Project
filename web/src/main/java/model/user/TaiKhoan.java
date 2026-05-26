package model.user;

/**
 * Lớp đại diện cho thực thể Tài Khoản trong hệ thống.
 * Chứa thông tin đăng nhập và phân quyền của người dùng.
 */
public class TaiKhoan {
    private int idTaiKhoan;
    private String email;
    private String matKhau;
    private String vaiTro;

    /**
     * Constructor mặc định không tham số.
     */
    public TaiKhoan() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng TaiKhoan.
     *
     * @param idTaiKhoan Mã định danh duy nhất của tài khoản
     * @param email Địa chỉ email dùng để đăng nhập
     * @param matKhau Mật khẩu đã được mã hóa
     * @param vaiTro Vai trò của người dùng (ADMIN, KHACH_HANG, SHOP, SHIPPER)
     */
    public TaiKhoan(int idTaiKhoan, String email, String matKhau, String vaiTro) {
        this.idTaiKhoan = idTaiKhoan;
        this.email = email;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public TaiKhoan(String email, String matKhau, String vaiTro) {
        this.email = email;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    // Getter và Setter

    /**
     * @return ID của tài khoản.
     */
    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    /**
     * @param idTaiKhoan ID tài khoản cần thiết lập.
     */
    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    /**
     * @return Địa chỉ email của tài khoản.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email Email cần thiết lập cho tài khoản.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Mật khẩu của tài khoản.
     */
    public String getMatKhau() {
        return matKhau;
    }

    /**
     * @param matKhau Mật khẩu cần thiết lập.
     */
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    /**
     * @return Vai trò hiện tại của tài khoản.
     */
    public String getVaiTro() {
        return vaiTro;
    }

    /**
     * @param vaiTro Vai trò cần gán cho tài khoản.
     */
    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}