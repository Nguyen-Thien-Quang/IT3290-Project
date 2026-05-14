package model.order;

/**
 * Lớp đại diện cho thực thể Voucher (Mã giảm giá).
 * Lưu trữ thông tin về loại ưu đãi và khách hàng sở hữu mã đó.
 */
public class Voucher {
    private int idVoucher;
    private int idKhachHang;
    private String loaiVoucher;

    /**
     * Constructor mặc định.
     */
    public Voucher() {}

    /**
     * Constructor đầy đủ tham số.
     *
     * @param idVoucher Mã định danh duy nhất của voucher
     * @param idKhachHang ID khách hàng sở hữu voucher
     * @param loaiVoucher Tên hoặc mô tả loại voucher
     */
    public Voucher(int idVoucher, int idKhachHang, String loaiVoucher) {
        this.idVoucher = idVoucher;
        this.idKhachHang = idKhachHang;
        this.loaiVoucher = loaiVoucher;
    }

    // Getter và Setter

    /**
     * @return ID của voucher.
     */
    public int getIdVoucher() {
        return idVoucher;
    }

    /**
     * @param idVoucher ID voucher cần thiết lập.
     */
    public void setIdVoucher(int idVoucher) {
        this.idVoucher = idVoucher;
    }

    /**
     * @return ID của khách hàng sở hữu mã này.
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
     * @return Tên hoặc loại của voucher.
     */
    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    /**
     * @param loaiVoucher Loại voucher cần thiết lập.
     */
    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }
}