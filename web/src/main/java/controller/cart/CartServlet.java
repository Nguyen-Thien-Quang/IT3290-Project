package controller.cart;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.GioHangDAO;
import model.order.GioHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// get current active user's cart and its items
@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {
    private final GioHangDAO gioHangDAO = new GioHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        if (customerId == null) {
            return;
        }

        GioHang cart = gioHangDAO.getActiveCart(customerId);
        if (cart == null) {
            Map<String, Object> data = new HashMap<>();
            data.put("cart", null);
            data.put("items", java.util.Collections.emptyList());
            JsonResponse.ok(resp, "Cart retrieved successfully", data);
            return;
        }

        List<model.order.GioHangMonAn> items = gioHangDAO.getItemsByCartId(cart.getIdGioHang());

        Map<String, Object> data = new HashMap<>();
        data.put("cart", cart);
        data.put("items", items);

        JsonResponse.ok(resp, "Cart retrieved successfully", data);
    }
}