package controller.DonHang;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/api/cart")
public class GioHangServlet extends HttpServlet {
    // GET: trả về thông tin các món ăn đang có trong giỏ hàng của người dùng hiện tại
    // POST thêm món ăn vào giỏ hàng, MonAnID và số lượng sẽ có trong request body
    // DELETE: xóa món ăn khỏi giỏ hàng
}
