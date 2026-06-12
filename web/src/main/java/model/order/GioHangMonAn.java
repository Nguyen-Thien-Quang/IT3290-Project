package model.order;

/**
 * Lớp trung gian kết nối Giỏ Hàng và Món Ăn.
 */
public class GioHangMonAn {
    private int idGioHang;
    private int idMonAn;
    private int soLuong;

    // Attributes from MonAn
    private int idCuaHang;
    private int idLoai;
    private String tenMon;
    private String trangThai;
    private double gia;
    private String img;

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

    /**
     * Constructor đầy đủ tham số.
     */
    public GioHangMonAn(int idGioHang, int idMonAn, int soLuong, int idCuaHang, int idLoai, String tenMon, String trangThai, double gia, String img) {
        this.idGioHang = idGioHang;
        this.idMonAn = idMonAn;
        this.soLuong = soLuong;
        this.idCuaHang = idCuaHang;
        this.idLoai = idLoai;
        this.tenMon = tenMon;
        this.trangThai = trangThai;
        this.gia = gia;
        this.img = img;
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

    public int getIdCuaHang() {
        return idCuaHang;
    }

    public void setIdCuaHang(int idCuaHang) {
        this.idCuaHang = idCuaHang;
    }

    public int getIdLoai() {
        return idLoai;
    }

    public void setIdLoai(int idLoai) {
        this.idLoai = idLoai;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}