package dao.report;

import context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp DAO (Data Access Object) chuyên trách xử lý các báo cáo và số liệu thống kê.
 * Sử dụng các lệnh EXEC để gọi Stored Procedure từ SQL Server nhằm tối ưu hóa hiệu năng
 * tính toán dữ liệu lớn (bán chạy, doanh thu) trực tiếp từ phía Database.
 * * @author Nguyễn Tiến Quân
 */
public class ThongKeDAO {

    /**
     * Lấy danh sách Top K món ăn bán chạy nhất của một cửa hàng dựa trên tổng số lượng đã bán.
     * Thích hợp để hiển thị danh sách "Món ăn yêu thích" hoặc gợi ý cho thực khách.
     * * @param storeId Mã định danh duy nhất (ID) của cửa hàng cần thống kê.
     * @param topK Số lượng món ăn tối đa muốn lấy ra trong danh sách (ví dụ: Top 5, Top 10).
     * @return Danh sách các {@link Map} chứa thông tin món ăn. Mỗi Map bao gồm các thuộc tính:
     * <ul>
     * <li>{@code idMonAn} (int): ID của món ăn</li>
     * <li>{@code tenMon} (String): Tên của món ăn</li>
     * <li>{@code totalSold} (int): Tổng số lượng sản phẩm đã bán thành công</li>
     * </ul>
     */
    public List<Map<String, Object>> getBestSellerByQuantity(int storeId, int topK) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "EXEC best_seller_by_quantity ?, ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            ps.setInt(2, topK);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idMonAn", rs.getInt("ID_MONAN"));
                    map.put("tenMon", rs.getNString("TENMON"));
                    map.put("totalSold", rs.getInt("TotalSold"));
                    resultList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Lấy danh sách Top K món ăn mang lại doanh thu cao nhất cho một cửa hàng.
     * Giúp chủ cửa hàng đánh giá chính xác mặt hàng nào đang mang lại nguồn lợi nhuận lớn nhất.
     * * @param storeId Mã định danh duy nhất (ID) của cửa hàng cần thống kê.
     * @param topK Số lượng món ăn tối đa muốn lấy ra trong danh sách (ví dụ: Top 3, Top 5).
     * @return Danh sách các {@link Map} chứa thông tin doanh thu. Mỗi Map bao gồm các thuộc tính:
     * <ul>
     * <li>{@code idMonAn} (int): ID của món ăn</li>
     * <li>{@code tenMon} (String): Tên của món ăn</li>
     * <li>{@code totalRevenue} (double): Tổng số tiền thu được từ món ăn này (Số lượng * Giá món)</li>
     * </ul>
     */
    public List<Map<String, Object>> getBestSellerByRevenue(int storeId, int topK) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "EXEC sp_best_seller_by_revenue ?, ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            ps.setInt(2, topK);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idMonAn", rs.getInt("ID_MONAN"));
                    map.put("tenMon", rs.getNString("TENMON"));
                    map.put("totalRevenue", rs.getDouble("TotalRevenue"));
                    resultList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Thống kê tổng doanh thu của một cửa hàng trong giai đoạn cụ thể.
     * Trả về kiểu float.
     */
    public float getDoanhThuTheoGiaiDoan(int storeId, Date startDate, Date endDate) {
        // Đổi biến lưu trữ thành float và gán giá trị mặc định là 0f
        float tongDoanhThu = 0f;

        String sql = "SELECT SUM(D.TONGTIEN) AS TONG_DOANHTHU "
                + "FROM DONHANG D "
                + "WHERE D.TRANGTHAI = N'Đã giao' "
                + "  AND CAST(D.THOIGIANDAT AS DATE) >= ? "
                + "  AND CAST(D.THOIGIANDAT AS DATE) <= ? "
                + "  AND D.ID_GIOHANG IN ( "
                + "      SELECT DISTINCT GM.ID_GIOHANG "
                + "      FROM GIOHANG_MONAN GM "
                + "      JOIN MONAN M ON GM.ID_MONAN = M.ID_MONAN "
                + "      WHERE M.ID_CUAHANG = ? "
                + "  )";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setInt(3, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Sử dụng getFloat để lấy dữ liệu
                    tongDoanhThu = rs.getFloat("TONG_DOANHTHU");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongDoanhThu;
    }
}