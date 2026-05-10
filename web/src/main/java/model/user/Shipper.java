package model.user;

import java.sql.Date;

/**
 * Lớp đại diện cho thực thể Shipper trong hệ thống.
 * Quản lý thông tin cá nhân và kết nối với tài khoản đăng nhập của Shipper.
 *
 */
public class Shipper {
    private int idShipper;
    private int idTaiKhoan;
    private String hoTen;
    private Date ngaySinh;
    private String sdt;

    /**
     * Constructor mặc định không tham số.
     */
    public Shipper() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng Shipper.
     *
     * @param idShipper Mã định danh duy nhất của shipper
     * @param idTaiKhoan ID tài khoản liên kết (Khóa ngoại từ bảng TAIKHOAN)
     * @param hoTen Họ và tên đầy đủ của shipper
     * @param ngaySinh Ngày tháng năm sinh
     * @param sdt Số điện thoại liên lạc dùng để gọi cho khách
     */
    public Shipper(int idShipper, int idTaiKhoan, String hoTen, Date ngaySinh, String sdt) {
        this.idShipper = idShipper;
        this.idTaiKhoan = idTaiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
    }

    // Getter và Setter

    /**
     * @return ID của shipper.
     */
    public int getIdShipper() {
        return idShipper;
    }

    /**
     * @param idShipper ID shipper cần thiết lập.
     */
    public void setIdShipper(int idShipper) {
        this.idShipper = idShipper;
    }

    /**
     * @return ID tài khoản liên kết của shipper.
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
     * @return Họ và tên của shipper.
     */
    public String getHoTen() {
        return hoTen;
    }

    /**
     * @param hoTen Họ tên cần thiết lập.
     */
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    /**
     * @return Ngày sinh của shipper.
     */
    public Date getNgaySinh() {
        return ngaySinh;
    }

    /**
     * @param ngaySinh Ngày sinh cần thiết lập.
     */
    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    /**
     * @return Số điện thoại của shipper.
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
}