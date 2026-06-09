package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/order")
public class DonHangServlet extends HttpServlet {
    // POST, PUT: lấy cart từ session, thay đổi trạng thái thành đã đặt
    // nhận trong body request thông tin về phương thức thanh toán và voucher
    // tạo đơn hàng tương ứng

    // GET /api/order?status=pending : lấy danh sách đơn hàng đang chờ giao
    // GET /api/order?status=shipping : lấy danh sách đơn hàng đang được giao
}
