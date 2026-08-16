package controller.DonHang;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.order.DonHangDAO;
import dao.order.GioHangDAO;
import model.order.GioHang;
import model.order.GioHangMonAn;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet handling shopping cart operations for Customers.
 * Supports viewing, adding, updating, and removing items, as well as checking out.
 */
@WebServlet("/api/cart")
public class GioHangServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final GioHangDAO gioHangDAO = new GioHangDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    /**
     * GET: Returns the current active cart and its items for the logged-in customer.
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

        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only customers have carts");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            GioHang cart = gioHangDAO.getActiveCart(customerId);
            responseMap.put("success", true);
            
            if (cart == null) {
                // Return empty cart structure
                responseMap.put("message", "No active cart found");
                responseMap.put("cart", null);
                responseMap.put("items", new java.util.ArrayList<>());
            } else {
                List<GioHangMonAn> items = gioHangDAO.getItemsByCartId(cart.getIdGioHang());
                
                // Recalculate total cost to ensure UI accuracy and avoid sync issues with DB triggers
                double calculatedTotal = 0;
                for (GioHangMonAn item : items) {
                    calculatedTotal += item.getGia() * item.getSoLuong();
                }
                cart.setTongTien(calculatedTotal);
                
                responseMap.put("cart", cart);
                responseMap.put("items", items);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * POST: Add item to cart or Checkout.
     * action=order -> Checkout
     * default -> Add item
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        if (session == null || session.getAttribute("customerId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a customer");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        String action = req.getParameter("action");

        try {
            if ("order".equalsIgnoreCase(action)) {
                // Checkout Logic
                handleCheckout(req, resp, customerId, responseMap);
            } else {
                // Add Item Logic
                handleAddItem(req, resp, customerId, responseMap);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
            resp.getWriter().write(gson.toJson(responseMap));
        }
    }

    private void handleAddItem(HttpServletRequest req, HttpServletResponse resp, int customerId, Map<String, Object> responseMap) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
        if (json == null || !json.has("monAnId") || !json.has("quantity")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing monAnId or quantity");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        int monAnId = json.get("monAnId").getAsInt();
        int quantity = json.get("quantity").getAsInt();

        GioHang cart = gioHangDAO.getActiveCart(customerId);
        int cartId;
        if (cart == null) {
            cartId = gioHangDAO.createCart(customerId);
        } else {
            cartId = cart.getIdGioHang();
        }

        if (cartId != -1) {
            gioHangDAO.addItemToCart(cartId, monAnId, quantity);
            responseMap.put("success", true);
            responseMap.put("message", "Item added to cart");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Failed to create/retrieve cart");
        }
        resp.getWriter().write(gson.toJson(responseMap));
    }

    private void handleCheckout(HttpServletRequest req, HttpServletResponse resp, int customerId, Map<String, Object> responseMap) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
        if (json == null || !json.has("phuongThuc")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing payment method (phuongThuc)");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        String phuongThuc = json.get("phuongThuc").getAsString();
        Integer voucherId = (json.has("voucherId") && !json.get("voucherId").isJsonNull()) ? json.get("voucherId").getAsInt() : null;

        GioHang cart = gioHangDAO.getActiveCart(customerId);
        if (cart == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "No active cart to checkout");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        boolean success = donHangDAO.makeDonHang(cart.getIdGioHang(), customerId, phuongThuc, voucherId);
        if (success) {
            responseMap.put("success", true);
            responseMap.put("message", "Order placed successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Failed to place order");
        }
        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * PUT: Update item quantity in cart.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        if (session == null || session.getAttribute("customerId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        int customerId = (int) session.getAttribute("customerId");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }

            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
            if (json == null || !json.has("monAnId") || !json.has("quantity")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Missing monAnId or quantity");
                resp.getWriter().write(gson.toJson(responseMap));
                return;
            }

            int monAnId = json.get("monAnId").getAsInt();
            int quantity = json.get("quantity").getAsInt();

            GioHang cart = gioHangDAO.getActiveCart(customerId);
            if (cart != null) {
                boolean success = gioHangDAO.updateSoLuongMonAnInGioHang(cart.getIdGioHang(), monAnId, quantity);
                responseMap.put("success", success);
                responseMap.put("message", success ? "Quantity updated" : "Failed to update quantity");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMap.put("success", false);
                responseMap.put("message", "No active cart found");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * DELETE: Remove item from cart.
     * Expects monAnId as a query parameter or from body. Based on common practice, query param is easy.
     * The comment says "monAnID lấy từ path param", but since this is mapped to /api/cart, 
     * it's easier to use a query param /api/cart?monAnId=123.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Map<String, Object> responseMap = new HashMap<>();

        if (session == null || session.getAttribute("customerId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        String monAnIdStr = req.getParameter("monAnId");

        if (monAnIdStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing monAnId");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            int monAnId = Integer.parseInt(monAnIdStr);
            GioHang cart = gioHangDAO.getActiveCart(customerId);
            if (cart != null) {
                boolean success = gioHangDAO.removeMonAnFromGioHang(cart.getIdGioHang(), monAnId);
                responseMap.put("success", success);
                responseMap.put("message", success ? "Item removed" : "Item not found in cart");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMap.put("success", false);
                responseMap.put("message", "No active cart found");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}