package dao.food;

import context.DBContext;
import model.food.LoaiMonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý các thao tác dữ liệu liên quan đến bảng LOAIMONAN.
 * Cung cấp danh mục các loại món ăn để hiển thị trên giao diện người dùng.
 */
public class LoaiMonAnDAO {

    /**
     * Lấy danh sách tất cả các loại món ăn có trong hệ thống.
     *
     * @return Danh sách các đối tượng {@link LoaiMonAn}.
     */
    public List<LoaiMonAn> getAll() {
        List<LoaiMonAn> list = new ArrayList<>();
        String sql = "SELECT ID_LOAI, TENLOAI, IMG FROM LOAIMONAN";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new LoaiMonAn(
                        rs.getInt("ID_LOAI"),
                        rs.getNString("TENLOAI"),
                        rs.getString("IMG")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm loại món ăn cụ thể dựa trên mã định danh.
     *
     * @param id Mã của loại món ăn cần tìm.
     * @return Đối tượng {@link LoaiMonAn} nếu tìm thấy, ngược lại trả về null.
     */
    public LoaiMonAn getById(int id) {
        String sql = "SELECT ID_LOAI, TENLOAI, IMG FROM LOAIMONAN WHERE ID_LOAI = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoaiMonAn(
                            rs.getInt("ID_LOAI"),
                            rs.getNString("TENLOAI"),
                            rs.getString("IMG")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}