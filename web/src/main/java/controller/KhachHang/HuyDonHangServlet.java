package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dao.order.DonHangDAO;
import model.order.DonHang;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet handling active order management for Customers.
 * Supports viewing active orders (Pending/Shipping) and canceling pending orders.
 */
@WebServlet("/api/order/customer")
public class HuyDonHangServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    /**
     * GET: Returns active orders (Chờ xác nhận or Đang giao) for the logged-in customer.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (!roles.CUSTOMER.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only customers can view their active orders");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Customer profile information missing from session");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            List<DonHang> activeOrders = donHangDAO.getActiveOrdersByKhachHang(customerId);
            responseMap.put("success", true);
            responseMap.put("data", activeOrders);
            responseMap.put("count", activeOrders.size());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * POST: Allows a customer to cancel a pending order.
     * Expects order 'id' as a query parameter.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only customers can cancel orders");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing order ID");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            int donHangId = Integer.parseInt(idStr);
            boolean success = donHangDAO.cancelOrder(donHangId, customerId);

            if (success) {
                responseMap.put("success", true);
                responseMap.put("message", "Order cancelled successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Could not cancel order. It may have already been accepted by a shipper or does not exist.");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Invalid order ID format");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}