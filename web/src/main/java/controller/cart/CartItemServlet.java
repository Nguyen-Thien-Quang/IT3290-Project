package controller.cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.GioHangDAO;
import model.order.GioHangMonAn;
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


// edit (update, add, delete) items in the current active user's cart
@WebServlet("/api/cart/items/*")
public class CartItemServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final GioHangDAO gioHangDAO = new GioHangDAO();

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        if (customerId == null) {
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Menu item ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int monAnId;
        try {
            monAnId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid menu item ID format");
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

        int quantity = 1;
        if (json != null && json.has("quantity") && !json.get("quantity").isJsonNull()) {
            quantity = json.get("quantity").getAsInt();
        }

        gioHangDAO.addItemToCart(customerId, monAnId, quantity);

        Map<String, Object> data = new HashMap<>();
        data.put("monAnId", monAnId);
        data.put("quantity", quantity);

        JsonResponse.ok(resp, "Item added to cart successfully", data);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        if (customerId == null) {
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Menu item ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int monAnId;
        try {
            monAnId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid menu item ID format");
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

        int quantity = 0;
        if (json != null && json.has("quantity") && !json.get("quantity").isJsonNull()) {
            quantity = json.get("quantity").getAsInt();
        }

        boolean updated = gioHangDAO.updateSoLuongMonAnInGioHang(customerId, monAnId, quantity);
        if (!updated) {
            JsonResponse.notFound(resp, "Item not found in cart");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("monAnId", monAnId);
        data.put("quantity", quantity);

        JsonResponse.ok(resp, "Cart item quantity updated successfully", data);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        if (customerId == null) {
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Menu item ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int monAnId;
        try {
            monAnId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid menu item ID format");
            return;
        }

        boolean removed = gioHangDAO.removeMonAnFromGioHang(customerId, monAnId);
        if (!removed) {
            JsonResponse.notFound(resp, "Item not found in cart");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("monAnId", monAnId);

        JsonResponse.ok(resp, "Item removed from cart successfully", data);
    }
}