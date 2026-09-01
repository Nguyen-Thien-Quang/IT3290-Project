package controller.shop;

import controller.utility.JsonResponse;
import dao.user.CuaHangDAO;
import model.user.CuaHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// endpoint for shop details info
@WebServlet("/api/shops/*")
public class ShopDetailServlet extends HttpServlet {
    private final CuaHangDAO cuaHangDAO = new CuaHangDAO();

    // ex: /api/shops/4
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Shop ID is required");
            return;
        }

        String idStr = pathInfo.substring(1); // remove leading /
        int shopId;
        try {
            shopId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid shop ID format");
            return;
        }

        CuaHang shop = cuaHangDAO.getStoreProfileById(shopId);
        if (shop == null) {
            JsonResponse.notFound(resp, "Shop not found");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("shop", shop);

        JsonResponse.ok(resp, "Shop retrieved successfully", data);
    }
}