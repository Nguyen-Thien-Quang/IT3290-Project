package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/order")
public class XemDonHangServlet extends HttpServlet {
    // GET: trả về danh sách các đơn hàng hiện tại trong trạng thái chờ giao
    // GET: trả về danh sách các đơn hàng đã được nhận bởi shipper
    // sử dụng query param: /api/order?status="đang chờ giao"
}
