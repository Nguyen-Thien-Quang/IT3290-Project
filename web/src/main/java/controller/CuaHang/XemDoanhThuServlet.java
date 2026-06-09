package controller.CuaHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/shop/sales")
public class XemDoanhThuServlet extends HttpServlet {
    //GET: trả về doanh thu của cửa hàng
    // check whether shop is login, check roles
    // get shop_id from session
    // nhận thông tin startDate và endDate trong bodyRequest
}
