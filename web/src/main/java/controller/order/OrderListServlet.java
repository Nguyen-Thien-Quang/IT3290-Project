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

// return list of orders for the authenticated user based on their role
// eg: GET /api/orders?scope=active for customer
// eg: GET /api/orders?status=pending for shipper
@WebServlet("/api/orders")
public class OrderListServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Check role via session directly to avoid multiple guards writing to response
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            JsonResponse.unauthorized(resp, "Authentication required");
            return;
        }

        model.user.TaiKhoan user = (model.user.TaiKhoan) session.getAttribute("user");
        String role = user.getVaiTro();

        Map<String, Object> data = new HashMap<>();
        List<DonHang> orders = null;

        if (roles.CUSTOMER.equals(role)) {
            Integer customerId = (Integer) session.getAttribute("customerId");
            if (customerId == null) {
                JsonResponse.internalError(resp, "Customer ID not found in session");
                return;
            }
            String scope = req.getParameter("scope");
            if ("active".equals(scope)) {
                orders = donHangDAO.getActiveOrdersByKhachHang(customerId);
            } else if ("history".equals(scope)) {
                orders = donHangDAO.getHistoryByKhachHang(customerId);
            } else {
                orders = donHangDAO.getActiveOrdersByKhachHang(customerId);
            }
            data.put("orders", orders);
            data.put("role", "customer");
        } else if (roles.SHOP.equals(role)) {
            Integer shopId = (Integer) session.getAttribute("shopId");
            if (shopId == null) {
                JsonResponse.internalError(resp, "Shop ID not found in session");
                return;
            }
            String status = req.getParameter("status");
            if (status != null) {
                orders = donHangDAO.getOrdersByStoreAndStatus(shopId, status);
            } else {
                orders = donHangDAO.getOrdersByStore(shopId);
            }
            data.put("orders", orders);
            data.put("role", "shop");
        } else if (roles.SHIPPER.equals(role)) {
            Integer shipperId = (Integer) session.getAttribute("shipperId");
            if (shipperId == null) {
                JsonResponse.internalError(resp, "Shipper ID not found in session");
                return;
            }
            String status = req.getParameter("status");
            if ("pending".equals(status)) {
                orders = donHangDAO.getPendingOrdersForShipper();
            } else if ("shipping".equals(status)) {
                orders = donHangDAO.getShippingOrdersByShipper(shipperId);
            } else if ("history".equals(req.getParameter("scope"))) {
                orders = donHangDAO.getDonHangsByShipper(shipperId);
            } else {
                orders = donHangDAO.getPendingOrdersForShipper();
            }
            data.put("orders", orders);
            data.put("role", "shipper");
        } else {
            JsonResponse.forbidden(resp, "Unknown role");
            return;
        }

        JsonResponse.ok(resp, "Orders retrieved successfully", data);
    }
}