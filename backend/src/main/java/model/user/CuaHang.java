package model.user;

/**
 * Lớp đại diện cho thực thể Cửa Hàng trong hệ thống.
 * Lưu trữ thông tin kinh doanh, địa chỉ và doanh thu tích lũy của cửa hàng
 */
public class CuaHang {
    private int idCuaHang;
    private int idTaiKhoan;
    private String tenCuaHang;
    private String diaChi;
    private String sdt;
    private double doanhThu;

    /**
     * Constructor mặc định không tham số.
     */
    public CuaHang() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng CuaHang.
     *
     * @param idCuaHang Mã định danh duy nhất của cửa hàng
     * @param idTaiKhoan ID tài khoản liên kết (Khóa ngoại)
     * @param tenCuaHang Tên hiển thị của cửa hàng
     * @param diaChi Địa chỉ vật lý của cửa hàng
     * @param sdt Số điện thoại liên hệ của cửa hàng
     * @param doanhThu Tổng doanh thu tích lũy
     */
    public CuaHang(int idCuaHang, int idTaiKhoan, String tenCuaHang, String diaChi, String sdt, double doanhThu) {
        this.idCuaHang = idCuaHang;
        this.idTaiKhoan = idTaiKhoan;
        this.tenCuaHang = tenCuaHang;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.doanhThu = doanhThu;
    }

    // Getter và Setter

    /**
     * @return ID của cửa hàng.
     */
    public int getIdCuaHang() {
        return idCuaHang;
    }

    /**
     * @param idCuaHang ID cửa hàng cần thiết lập.
     */
    public void setIdCuaHang(int idCuaHang) {
        this.idCuaHang = idCuaHang;
    }

    /**
     * @return ID tài khoản liên kết của chủ cửa hàng.
     */
    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    /**
     * @param idTaiKhoan ID tài khoản cần liên kết.
     */
    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    /**
     * @return Tên của cửa hàng.
     */
    public String getTenCuaHang() {
        return tenCuaHang;
    }

    /**
     * @param tenCuaHang Tên cửa hàng cần thiết lập.
     */
    public void setTenCuaHang(String tenCuaHang) {
        this.tenCuaHang = tenCuaHang;
    }

    /**
     * @return Địa chỉ của cửa hàng.
     */
    public String getDiaChi() {
        return diaChi;
    }

    /**
     * @param diaChi Địa chỉ cửa hàng cần thiết lập.
     */
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    /**
     * @return Số điện thoại liên hệ của cửa hàng.
     */
    public String getSdt() {
        return sdt;
    }

    /**
     * @param sdt Số điện thoại cần thiết lập.
     */
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    /**
     * @return Doanh thu hiện tại của cửa hàng.
     */
    public double getDoanhThu() {
        return doanhThu;
    }

    /**
     * @param doanhThu Giá trị doanh thu cần cập nhật.
     */
    public void setDoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
    }
}