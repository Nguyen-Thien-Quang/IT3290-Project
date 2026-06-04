package controller.GioHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/order")
public class DatHangServlet extends HttpServlet {
    // POST, PUT: lấy cart từ session, thay đổi trạng thái thành đã đặt
    // nhận trong body request thông tin về phương thức thanh toán và voucher
    // tạo đơn hàng tương ứng
}
