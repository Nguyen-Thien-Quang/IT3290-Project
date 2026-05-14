package model.order;

import java.sql.Timestamp;

/**
 * Lớp đại diện cho thực thể Đơn Hàng.
 * Chứa toàn bộ thông tin về trạng thái, thời gian đặt, tổng tiền và các bên liên quan.
 */
public class DonHang {
    private int idDonHang;
    private int idGioHang;
    private int idKhachHang;
    private Integer idShipper; // Cho phép null khi đơn mới tạo chưa có người nhận
    private Integer idVoucher; // Cho phép null nếu không áp dụng mã giảm giá
    private Timestamp thoiGianDat;
    private String trangThai;
    private double tongTien;
    private String phuongThucThanhToan;

    /**
     * Constructor mặc định.
     */
    public DonHang() {}

    /**
     * Constructor đầy đủ tham số.
     *
     * @param idDonHang Mã đơn hàng
     * @param idGioHang ID giỏ hàng tương ứng
     * @param idKhachHang ID khách hàng đặt đơn
     * @param idShipper ID shipper nhận đơn (có thể null)
     * @param idVoucher ID voucher áp dụng (có thể null)
     * @param thoiGianDat Thời điểm xác nhận đặt hàng
     * @param trangThai Trạng thái đơn (cho_xac_nhan, dang_giao, da_giao, da_huy)
     * @param tongTien Tổng giá trị thanh toán cuối cùng
     * @param phuongThucThanhToan Hình thức thanh toán (Tiền mặt, Chuyển khoản,...)
     */
    public DonHang(int idDonHang, int idGioHang, int idKhachHang, Integer idShipper,
                   Integer idVoucher, Timestamp thoiGianDat, String trangThai,
                   double tongTien, String phuongThucThanhToan) {
        this.idDonHang = idDonHang;
        this.idGioHang = idGioHang;
        this.idKhachHang = idKhachHang;
        this.idShipper = idShipper;
        this.idVoucher = idVoucher;
        this.thoiGianDat = thoiGianDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    // Getter và Setter

    public int getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(int idDonHang) {
        this.idDonHang = idDonHang;
    }

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

    public Integer getIdShipper() {
        return idShipper;
    }

    public void setIdShipper(Integer idShipper) {
        this.idShipper = idShipper;
    }

    public Integer getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(Integer idVoucher) {
        this.idVoucher = idVoucher;
    }

    public Timestamp getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(Timestamp thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
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

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
}