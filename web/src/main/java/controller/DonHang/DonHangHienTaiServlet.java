package controller.DonHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dao.order.DonHangDAO;
import dao.user.ShipperDAO;
import model.order.DonHang;
import model.user.Shipper;
import model.user.TaiKhoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.roles;

/**
 * Servlet handling requests for current pending orders for shippers.
 * This servlet processes GET requests to retrieve orders with 'Chờ xác nhận' status.
 * It verifies the shipper's session and retrieves data using DonHangDAO.
 */
@WebServlet("/api/order")
public class DonHangHienTaiServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();

    /**
     * Handles GET requests to retrieve all current pending orders for shippers.
     * 
     * @param req  the HttpServletRequest object
     * @param resp the HttpServletResponse object used to return the order list as JSON
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON and encoding to UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");

        Map<String, Object> responseMap = new HashMap<>();

        // 1. Authorization: Verify if user is logged in and has the Shipper role
        if (user == null || !roles.SHIPPER.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a Shipper");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            // 2. Data Retrieval: Fetch pending orders using DonHangDAO
            DonHangDAO dhdao = new DonHangDAO();
            List<DonHang> pendingOrders = dhdao.getPendingOrdersForShipper();

            // 3. Prepare successful response
            responseMap.put("success", true);
            responseMap.put("data", pendingOrders);
            responseMap.put("count", pendingOrders.size());
            
        } catch (Exception e) {
            // Handle unexpected database or system errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        // Send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * Handles POST requests for shippers to accept a pending order.
     * Changes the order status to 'Đang giao' and assigns the shipper.
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

        HttpSession session = req.getSession();
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        Map<String, Object> responseMap = new HashMap<>();

        // 1. Authorization: Verify if user is logged in and has the Shipper role
        if (user == null || !roles.SHIPPER.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a Shipper");
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

            // 3. Get Shipper Profile to obtain Shipper ID
            ShipperDAO shipperDAO = new ShipperDAO();
            Shipper shipper = shipperDAO.getShipperByAccountId(user.getIdTaiKhoan());

            if (shipper == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                responseMap.put("success", false);
                responseMap.put("message", "Shipper profile not found");
                resp.getWriter().write(gson.toJson(responseMap));
                return;
            }

            // 4. Accept Order using DAO
            DonHangDAO dhdao = new DonHangDAO();
            boolean success = dhdao.acceptOrder(donHangId, shipper.getIdShipper());

            if (success) {
                responseMap.put("success", true);
                responseMap.put("message", "Order accepted successfully. Status changed to 'Đang giao'");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Could not accept order. It may have already been taken or does not exist.");
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

        // Send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
