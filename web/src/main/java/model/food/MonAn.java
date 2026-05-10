package model.food;

/**
 * Lớp đại diện cho thực thể Món Ăn trong hệ thống.
 * Chứa thông tin chi tiết về món ăn, giá cả, trạng thái kinh doanh và hình ảnh minh họa.
 */
public class MonAn {
    private int idMonAn;
    private int idCuaHang;
    private int idLoai;
    private String tenMon;
    private String trangThai; // con_ban, het_hang, ngung_kinh_doanh
    private double gia;
    private String img;

    /**
     * Constructor mặc định không tham số.
     */
    public MonAn() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng MonAn.
     *
     * @param idMonAn Mã định danh duy nhất của món ăn
     * @param idCuaHang ID của cửa hàng sở hữu món ăn này
     * @param idLoai ID loại món ăn (danh mục)
     * @param tenMon Tên của món ăn
     * @param trangThai Trạng thái món ăn (còn bán, hết hàng, ngừng kinh doanh)
     * @param gia Giá bán của món ăn
     * @param img Đường dẫn hoặc link hình ảnh của món ăn
     */
    public MonAn(int idMonAn, int idCuaHang, int idLoai, String tenMon, String trangThai, double gia, String img) {
        this.idMonAn = idMonAn;
        this.idCuaHang = idCuaHang;
        this.idLoai = idLoai;
        this.tenMon = tenMon;
        this.trangThai = trangThai;
        this.gia = gia;
        this.img = img;
    }

    // Getter và Setter

    /**
     * @return ID của món ăn.
     */
    public int getIdMonAn() {
        return idMonAn;
    }

    /**
     * @param idMonAn ID món ăn cần thiết lập.
     */
    public void setIdMonAn(int idMonAn) {
        this.idMonAn = idMonAn;
    }

    /**
     * @return ID của cửa hàng sở hữu món ăn.
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
     * @return ID loại món ăn.
     */
    public int getIdLoai() {
        return idLoai;
    }

    /**
     * @param idLoai ID loại món ăn cần thiết lập.
     */
    public void setIdLoai(int idLoai) {
        this.idLoai = idLoai;
    }

    /**
     * @return Tên của món ăn.
     */
    public String getTenMon() {
        return tenMon;
    }

    /**
     * @param tenMon Tên món ăn cần thiết lập.
     */
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    /**
     * @return Trạng thái hiện tại của món ăn.
     */
    public String getTrangThai() {
        return trangThai;
    }

    /**
     * @param trangThai Trạng thái cần thiết lập (con_ban, het_hang, ngung_kinh_doanh).
     */
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    /**
     * @return Giá bán của món ăn.
     */
    public double getGia() {
        return gia;
    }

    /**
     * @param gia Giá món ăn cần thiết lập.
     */
    public void setGia(double gia) {
        this.gia = gia;
    }

    /**
     * @return Đường dẫn hình ảnh của món ăn.
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img Link ảnh cần thiết lập.
     */
    public void setImg(String img) {
        this.img = img;
    }
}