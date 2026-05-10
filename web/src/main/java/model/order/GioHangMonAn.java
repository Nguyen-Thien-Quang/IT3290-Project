package model.order;

/**
 * Lớp trung gian kết nối Giỏ Hàng và Món Ăn.
 */
public class GioHangMonAn {
    private int idGioHang;
    private int idMonAn;
    private int soLuong;

    /**
     * Constructor mặc định.
     */
    public GioHangMonAn() {}

    /**
     * @param idGioHang ID giỏ hàng
     * @param idMonAn ID món ăn được chọn
     * @param soLuong Số lượng món ăn
     */
    public GioHangMonAn(int idGioHang, int idMonAn, int soLuong) {
        this.idGioHang = idGioHang;
        this.idMonAn = idMonAn;
        this.soLuong = soLuong;
    }

    // Getter và Setter

    public int getIdGioHang() {
        return idGioHang;
    }

    public void setIdGioHang(int idGioHang) {
        this.idGioHang = idGioHang;
    }

    public int getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(int idMonAn) {
        this.idMonAn = idMonAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}