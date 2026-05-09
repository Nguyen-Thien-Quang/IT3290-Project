package model.user;

import java.sql.Date;

/**
 * Lớp đại diện cho thực thể Khách Hàng trong hệ thống.
 * Lưu trữ thông tin cá nhân và liên kết với tài khoản đăng nhập.
 */
public class KhachHang {
    private int idKhachHang;
    private int idTaiKhoan;
    private String hoTen;
    private Date ngaySinh;
    private String diaChi;
    private String sdt;

    /**
     * Constructor mặc định không tham số.
     */
    public KhachHang() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng KhachHang.
     *
     * @param idKhachHang Mã định danh duy nhất của khách hàng
     * @param idTaiKhoan ID tài khoản liên kết (Khóa ngoại)[cite: 1]
     * @param hoTen Họ và tên đầy đủ của khách hàng[cite: 1]
     * @param ngaySinh Ngày tháng năm sinh[cite: 1]
     * @param diaChi Địa chỉ thường trú hoặc địa chỉ giao hàng[cite: 1]
     * @param sdt Số điện thoại liên lạc[cite: 1]
     */
    public KhachHang(int idKhachHang, int idTaiKhoan, String hoTen, Date ngaySinh, String diaChi, String sdt) {
        this.idKhachHang = idKhachHang;
        this.idTaiKhoan = idTaiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    // Getter và Setter

    /**
     * @return ID của khách hàng.
     */
    public int getIdKhachHang() {
        return idKhachHang;
    }

    /**
     * @param idKhachHang ID khách hàng cần thiết lập.
     */
    public void setIdKhachHang(int idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    /**
     * @return ID tài khoản liên kết.
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
     * @return Họ và tên của khách hàng.
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
     * @return Ngày sinh của khách hàng.
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
     * @return Địa chỉ của khách hàng.
     */
    public String getDiaChi() {
        return diaChi;
    }

    /**
     * @param diaChi Địa chỉ cần thiết lập.
     */
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    /**
     * @return Số điện thoại của khách hàng.
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