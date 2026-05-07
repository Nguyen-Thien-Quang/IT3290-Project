package model.order;

/**
 * Lớp đại diện cho Giỏ Hàng của người dùng.
 */
public class GioHang {
    private int idGioHang;
    private int idKhachHang;
    private String trangThai; // dang_tao, da_dat, da_huy
    private double tongTien;

    /**
     * Constructor mặc định.
     */
    public GioHang() {}

    /**
     * @param idGioHang ID giỏ hàng
     * @param idKhachHang ID chủ sở hữu giỏ hàng
     * @param trangThai Trạng thái xử lý giỏ hàng
     * @param tongTien Tổng giá trị các món trong giỏ
     */
    public GioHang(int idGioHang, int idKhachHang, String trangThai, double tongTien) {
        this.idGioHang = idGioHang;
        this.idKhachHang = idKhachHang;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
    }

    // Getter và Setter

    public int getIdGioHang() {
        return idGioHang;
    }

    public void setIdGioHang(int idGioHang) {
        this.idGioHang = idGioHang;
    }

    public int getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(int idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}