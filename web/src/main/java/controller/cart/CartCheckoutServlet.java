package controller.cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.DonHangDAO;
import dao.order.GioHangDAO;
import model.order.DonHang;
import model.order.GioHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/cart/checkout")
public class CartCheckoutServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final GioHangDAO gioHangDAO = new GioHangDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        if (customerId == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject json;
        try {
            json = gson.fromJson(sb.toString(), JsonObject.class);
        } catch (Exception e) {
            JsonResponse.badRequest(resp, "Invalid JSON format");
            return;
        }

        String phuongThuc = null;
        if (json != null && json.has("phuongThuc") && !json.get("phuongThuc").isJsonNull()) {
            phuongThuc = json.get("phuongThuc").getAsString();
        }

        Integer voucherId = null;
        if (json != null && json.has("voucherId") && !json.get("voucherId").isJsonNull()) {
            voucherId = json.get("voucherId").getAsInt();
        }

        GioHang cart = gioHangDAO.getActiveCart(customerId);
        if (cart == null) {
            JsonResponse.badRequest(resp, "No active cart found");
            return;
        }

        boolean success = donHangDAO.makeDonHang(cart.getIdGioHang(), customerId, phuongThuc, voucherId);
        if (!success) {
            JsonResponse.internalError(resp, "Failed to place order. Cart may be empty or invalid.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orderPlaced", true);

        JsonResponse.ok(resp, "Order placed successfully", data);
    }
}