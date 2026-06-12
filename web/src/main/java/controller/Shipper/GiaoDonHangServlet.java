package controller.Shipper;

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
 * Servlet handling requests for orders currently being delivered by a shipper.
 * This servlet allows shippers to view their active deliveries and mark them as delivered.
 * 
 * Endpoints:
 * - GET /api/order/shipping : Retrieves all orders with status 'Đang giao' for the logged-in shipper.
 * - POST /api/order/shipping : Updates an order's status to 'Đã giao' (Delivered).
 */
@WebServlet("/api/order/shipping")
public class GiaoDonHangServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();
    /** DAO for order-related database operations */
    private final DonHangDAO donHangDAO = new DonHangDAO();

    /**
     * Handles GET requests to retrieve orders currently being shipped by the authenticated shipper.
     * 
     * @param req  the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        // 1. Authorization: Verify if user is logged in and has the Shipper role
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a Shipper");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (!roles.SHIPPER.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only Shippers can access this endpoint");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // 2. Retrieve Shipper ID from session
        Integer shipperId = (Integer) session.getAttribute("shipperId");
        if (shipperId == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Shipper profile information missing from session");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            // 3. Data Retrieval: Fetch shipping orders using DonHangDAO
            List<DonHang> shippingOrders = donHangDAO.getShippingOrdersByShipper(shipperId);

            // 4. Prepare successful response
            responseMap.put("success", true);
            responseMap.put("data", shippingOrders);
            responseMap.put("count", shippingOrders.size());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error while fetching shipping orders: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * Handles POST requests for shippers to confirm an order as delivered.
     * Updates the order status to 'Đã giao'.
     * 
     * @param req  the HttpServletRequest object containing 'id' parameter
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        // 1. Authorization: Verify if user is logged in and has the Shipper role
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a Shipper");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (!roles.SHIPPER.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only Shippers can update delivery status");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // 2. Extract and validate Order ID
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

            // 3. Confirm Delivery using DAO
            boolean success = donHangDAO.confirmDelivery(donHangId);

            if (success) {
                responseMap.put("success", true);
                responseMap.put("message", "Order successfully marked as 'Đã giao' (Delivered)");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Could not update order status. Please verify the order ID.");
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
