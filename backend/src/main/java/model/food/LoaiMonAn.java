package model.food;

/**
 * Lớp đại diện cho thực thể Loại Món Ăn trong hệ thống.
 * Dùng để phân loại các món ăn theo danh mục (ví dụ: Đồ khai vị, Món chính, Đồ uống).
 */
public class LoaiMonAn {
    private int idLoai;
    private String tenLoai;
    private String img;

    /**
     * Constructor mặc định không tham số.
     */
    public LoaiMonAn() {}

    /**
     * Constructor đầy đủ tham số để khởi tạo đối tượng LoaiMonAn.
     *
     * @param idLoai Mã định danh duy nhất của loại món ăn
     * @param tenLoai Tên của danh mục loại món ăn[cite: 1]
     * @param img Đường dẫn hoặc link hình ảnh đại diện cho loại món ăn[cite: 1]
     */
    public LoaiMonAn(int idLoai, String tenLoai, String img) {
        this.idLoai = idLoai;
        this.tenLoai = tenLoai;
        this.img = img;
    }

    // Getter và Setter

    /**
     * @return ID của loại món ăn.
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
     * @return Tên của loại món ăn.
     */
    public String getTenLoai() {
        return tenLoai;
    }

    /**
     * @param tenLoai Tên loại món ăn cần thiết lập.
     */
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    /**
     * @return Đường dẫn hình ảnh của loại món ăn.
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