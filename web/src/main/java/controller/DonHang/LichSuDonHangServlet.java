package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;


@WebServlet("/api/order/history")
public class LichSuDonHangServlet extends HttpServlet {
    // GET: cửa hàng xem lại những đơn hàng của cửa hàng: /api/order/history?role="Cửa hàng"
    // GET: Khách hàng xem lại lịch sử đơn đặt: /api/order/history?role="Khách hàng"

}
