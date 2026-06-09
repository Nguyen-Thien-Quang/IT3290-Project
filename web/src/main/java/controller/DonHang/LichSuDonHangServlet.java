package controller.DonHang;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet handling order history retrieval for both Customers and Shop Managers.
 * It provides historical order data based on the authenticated user's role stored in the session.
 * 
 * Endpoints:
 * - GET /api/order/history : Returns order history based on the logged-in user's role.
 */
@WebServlet("/api/order/history")
public class LichSuDonHangServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();
    /** DAO for order-related database operations */
    private final DonHangDAO donHangDAO = new DonHangDAO();

    /**
     * Handles GET requests to retrieve order history.
     * The role is automatically determined from the user's session.
     * 
     * @param req  the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON and encoding to UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        // 1. Authentication Check: Ensure the user is logged in
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "User not authenticated. Please login.");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        String userRole = user.getVaiTro();
        List<DonHang> history = new ArrayList<>();

        // 2. Role-based Logic: Fetch history based on the session role using role constants
        try {
            if (roles.CUSTOMER.equals(userRole)) {
                // Customer Path: Retrieve customerId from session
                Integer customerId = (Integer) session.getAttribute("customerId");
                if (customerId != null) {
                    history = donHangDAO.getHistoryByKhachHang(customerId);
                    responseMap.put("success", true);
                    responseMap.put("data", history);
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    responseMap.put("success", false);
                    responseMap.put("message", "Customer ID not found in session.");
                }
            } else if (roles.SHOP.equals(userRole)) {
                // Shop Manager Path: Retrieve shopId from session
                Integer shopId = (Integer) session.getAttribute("shopId");
                if (shopId != null) {
                    history = donHangDAO.getOrdersByStore(shopId);
                    responseMap.put("success", true);
                    responseMap.put("data", history);
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    responseMap.put("success", false);
                    responseMap.put("message", "Shop ID not found in session.");
                }
            } else {
                // Role not supported for history retrieval (e.g., Shipper or Admin in this specific endpoint)
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                responseMap.put("success", false);
                responseMap.put("message", "Access denied: Order history is only available for Customers and Shop Managers.");
            }
        } catch (Exception e) {
            // Handle unexpected database or processing errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "An error occurred while fetching order history: " + e.getMessage());
            e.printStackTrace();
        }

        // 3. Finalize and send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
