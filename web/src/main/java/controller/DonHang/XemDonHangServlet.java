package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/order")
public class XemDonHangServlet extends HttpServlet {
    // GET /api/order?status=pending : lấy danh sách đơn hàng đang chờ giao
    // GET /api/order?status=shipping : lấy danh sách đơn hàng đang được giao
}
