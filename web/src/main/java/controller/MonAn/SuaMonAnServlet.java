package controller.MonAn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.food.MonAnDAO;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet handling food management operations for Store owners.
 * Supports adding, updating, and deleting food items from the store's menu.
 */
@WebServlet("/api/food")
public class SuaMonAnServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final MonAnDAO monAnDAO = new MonAnDAO();

    /**
     * Helper method to verify if the current user is a Shop owner.
     */
    private Integer getStoreIdIfAuthorized(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> responseMap) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login");
            return null;
        }

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (!roles.SHOP.equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseMap.put("success", false);
            responseMap.put("message", "Access denied: Only shops can manage food items");
            return null;
        }

        Integer storeId = (Integer) session.getAttribute("storeId");
        if (storeId == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "Store profile missing from session");
            return null;
        }
        return storeId;
    }

    /**
     * POST: Add a new food item to the menu.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> responseMap = new HashMap<>();

        Integer storeId = getStoreIdIfAuthorized(req, resp, responseMap);
        if (storeId == null) {
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }

            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
            if (json == null || !json.has("tenMon") || !json.has("idLoai") || !json.has("gia")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Missing required fields (tenMon, idLoai, gia)");
                resp.getWriter().write(gson.toJson(responseMap));
                return;
            }

            String tenMon = json.get("tenMon").getAsString();
            int idLoai = json.get("idLoai").getAsInt();
            double gia = json.get("gia").getAsDouble();
            String img = json.has("img") && !json.get("img").isJsonNull() ? json.get("img").getAsString() : "";

            boolean success = monAnDAO.insertMonAn(storeId, idLoai, tenMon, gia, img);
            responseMap.put("success", success);
            responseMap.put("message", success ? "Food item added successfully" : "Failed to add food item");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * PUT: Update an existing food item.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> responseMap = new HashMap<>();

        Integer storeId = getStoreIdIfAuthorized(req, resp, responseMap);
        if (storeId == null) {
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }

            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
            if (json == null || !json.has("idMonAn") || !json.has("tenMon") || !json.has("gia") || !json.has("trangThai")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("success", false);
                responseMap.put("message", "Missing required fields (idMonAn, tenMon, gia, trangThai)");
                resp.getWriter().write(gson.toJson(responseMap));
                return;
            }

            int idMonAn = json.get("idMonAn").getAsInt();
            String tenMon = json.get("tenMon").getAsString();
            double gia = json.get("gia").getAsDouble();
            String trangThai = json.get("trangThai").getAsString();
            String img = json.has("img") && !json.get("img").isJsonNull() ? json.get("img").getAsString() : "";

            boolean success = monAnDAO.updateMonAn(idMonAn, storeId, tenMon, gia, trangThai, img);
            responseMap.put("success", success);
            responseMap.put("message", success ? "Food item updated successfully" : "Failed to update food item. Ensure you own this item.");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }

    /**
     * DELETE: Remove a food item from the menu.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> responseMap = new HashMap<>();

        Integer storeId = getStoreIdIfAuthorized(req, resp, responseMap);
        if (storeId == null) {
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing food ID parameter");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            int idMonAn = Integer.parseInt(idStr);
            boolean success = monAnDAO.deleteMonAn(idMonAn, storeId);

            responseMap.put("success", success);
            if (success) {
                responseMap.put("message", "Food item deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("message", "Failed to delete item. It may have existing orders or you do not have permission.");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Invalid food ID format");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}
