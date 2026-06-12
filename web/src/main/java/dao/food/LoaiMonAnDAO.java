package dao.food;

import context.DBContext;
import model.food.LoaiMonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến Danh mục/Loại món ăn.
 */
public class LoaiMonAnDAO {

    /**
     * Lấy toàn bộ danh sách loại món ăn (bao gồm cả hình ảnh) để hiển thị lên Trang chủ.
     * @return Danh sách các đối tượng {@link LoaiMonAn}.
     */
    public List<LoaiMonAn> getAll() {
        List<LoaiMonAn> list = new ArrayList<>();
        // Đã bổ sung thêm cột IMG vào câu truy vấn
        String sql = "SELECT ID_LOAI, TENLOAI, IMG FROM LOAIMONAN";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new LoaiMonAn(
                        rs.getInt("ID_LOAI"),
                        rs.getNString("TENLOAI"),
                        rs.getString("IMG") // Lấy đường dẫn/tên file ảnh từ database
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}