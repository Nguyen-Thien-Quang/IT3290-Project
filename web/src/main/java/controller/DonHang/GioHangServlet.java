package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/cart")
public class GioHangServlet extends HttpServlet {
    // GET: trả về thông tin các món ăn đang có trong giỏ hàng của người dùng hiện tại
    // POST /api/cart?action=edit thêm món ăn vào giỏ hàng, MonAnID và số lượng sẽ có trong request body
    // DELETE: xóa món ăn khỏi giỏ hàng
    // POST /api/cart?action=order đặt đơn hàng: thay đổi trạng thái cart thành đã đặt, nhận thông tin về phương thức thanh toán và voucher. tạo đơn hàng tương ứng
}
