package controller.shop;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.food.MonAnDAO;
import model.food.MonAn;
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

// CRUD for shop's menu items (food items)
// eg: GET /api/me/shop/menu — list own menu
// eg: POST /api/me/shop/menu — add food
// eg: PUT /api/me/shop/menu/{id} — update food
// eg: DELETE /api/me/shop/menu/{id} — delete food
@WebServlet("/api/me/shop/menu/*")
public class ShopMenuManageServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final MonAnDAO monAnDAO = new MonAnDAO();

    // GET /api/me/shop/menu — list own menu (all items, including out of stock)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        List<MonAn> menu = monAnDAO.getByStore(shopId);

        Map<String, Object> data = new HashMap<>();
        data.put("items", menu);
        data.put("count", menu.size());

        JsonResponse.ok(resp, "Menu retrieved successfully", data);
    }

    // POST /api/me/shop/menu — add food
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
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

        if (json == null || !json.has("tenMon") || !json.has("idLoai") || !json.has("gia")) {
            JsonResponse.badRequest(resp, "Missing required fields (tenMon, idLoai, gia)");
            return;
        }

        String tenMon = json.get("tenMon").getAsString();
        int idLoai = json.get("idLoai").getAsInt();
        double gia = json.get("gia").getAsDouble();
        String img = (json.has("img") && !json.get("img").isJsonNull()) ? json.get("img").getAsString() : "";

        boolean success = monAnDAO.insertMonAn(shopId, idLoai, tenMon, gia, img);
        if (!success) {
            JsonResponse.internalError(resp, "Failed to add food item");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("tenMon", tenMon);
        data.put("idLoai", idLoai);
        data.put("gia", gia);
        data.put("img", img);

        JsonResponse.created(resp, "Food item added successfully", data);
    }

    // PUT /api/me/shop/menu/{id} — update food
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Food ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int monAnId;
        try {
            monAnId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid food ID format");
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

        if (json == null || !json.has("tenMon") || !json.has("idLoai") || !json.has("gia") || !json.has("trangThai")) {
            JsonResponse.badRequest(resp, "Missing required fields (tenMon, idLoai, gia, trangThai)");
            return;
        }

        String tenMon = json.get("tenMon").getAsString();
        int idLoai = json.get("idLoai").getAsInt();
        double gia = json.get("gia").getAsDouble();
        String trangThai = json.get("trangThai").getAsString();
        String img = (json.has("img") && !json.get("img").isJsonNull()) ? json.get("img").getAsString() : "";

        boolean success = monAnDAO.updateMonAn(monAnId, shopId, tenMon, gia, trangThai, img, idLoai);
        if (!success) {
            JsonResponse.notFound(resp, "Food item not found or you do not have permission to update it");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idMonAn", monAnId);
        data.put("tenMon", tenMon);
        data.put("idLoai", idLoai);
        data.put("gia", gia);
        data.put("trangThai", trangThai);
        data.put("img", img);

        JsonResponse.ok(resp, "Food item updated successfully", data);
    }

    // DELETE /api/me/shop/menu/{id} — delete food
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Food ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int monAnId;
        try {
            monAnId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid food ID format");
            return;
        }

        boolean success = monAnDAO.deleteMonAn(monAnId, shopId);
        if (!success) {
            JsonResponse.badRequest(resp, "Failed to delete food item. It may have existing orders or you do not have permission.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idMonAn", monAnId);

        JsonResponse.ok(resp, "Food item deleted successfully", data);
    }
}
