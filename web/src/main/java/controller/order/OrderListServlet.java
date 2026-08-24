package controller.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.DonHangDAO;
import model.order.DonHang;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orders")
public class OrderListServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        Integer shopId = AuthGuard.requireShopId(req, resp);
        Integer shipperId = AuthGuard.requireShipperId(req, resp);

        Map<String, Object> data = new HashMap<>();
        List<DonHang> orders = null;
        String errorMsg = null;

        if (customerId != null) {
            // Customer role
            String scope = req.getParameter("scope");
            if ("active".equals(scope)) {
                orders = donHangDAO.getActiveOrdersByKhachHang(customerId);
            } else if ("history".equals(scope)) {
                orders = donHangDAO.getHistoryByKhachHang(customerId);
            } else {
                // Default: try to get active orders
                orders = donHangDAO.getActiveOrdersByKhachHang(customerId);
            }
            data.put("orders", orders);
            data.put("role", "customer");
        } else if (shopId != null) {
            // Shop role
            String status = req.getParameter("status");
            if (status != null) {
                orders = donHangDAO.getOrdersByStoreAndStatus(shopId, status);
            } else {
                // Default: get all orders for this store
                orders = donHangDAO.getOrdersByStore(shopId);
            }
            data.put("orders", orders);
            data.put("role", "shop");
        } else if (shipperId != null) {
            // Shipper role
            String status = req.getParameter("status");
            if ("pending".equals(status)) {
                orders = donHangDAO.getPendingOrdersForShipper();
            } else if ("shipping".equals(status)) {
                orders = donHangDAO.getShippingOrdersByShipper(shipperId);
            } else if ("history".equals(req.getParameter("scope"))) {
                orders = donHangDAO.getDonHangsByShipper(shipperId);
            } else {
                // Default: pending orders
                orders = donHangDAO.getPendingOrdersForShipper();
            }
            data.put("orders", orders);
            data.put("role", "shipper");
        } else {
            JsonResponse.unauthorized(resp, "Authentication required");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Orders retrieved successfully");
        response.put("data", data);

        JsonResponse.ok(resp, "Orders retrieved successfully", response);
    }
}