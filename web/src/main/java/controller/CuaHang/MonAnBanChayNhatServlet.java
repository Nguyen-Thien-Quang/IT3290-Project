package controller.CuaHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/shop/food/bestSeller")
public class MonAnBanChayNhatServlet extends HttpServlet {
    //GET: Tìm kiếm món ăn bán chạy nhất theo doanh thu của cửa hàng
    // nhận tham số là số nguyên K: trả về K món ăn bán chạy nhất (mặc định là 1)
}
