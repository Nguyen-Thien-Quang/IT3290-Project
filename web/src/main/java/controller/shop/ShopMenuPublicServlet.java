package controller.shop;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.JsonResponse;
import dao.food.MonAnDAO;
import model.food.MonAn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/shops/*/menu")
public class ShopMenuPublicServlet extends HttpServlet {
    private final MonAnDAO monAnDAO = new MonAnDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Shop ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int shopId;
        try {
            shopId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid shop ID format");
            return;
        }

        List<MonAn> menu = monAnDAO.getByStore(shopId);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("items", menu);
        pageInfo.put("count", menu.size());
        data.put("menu", pageInfo);

        JsonResponse.ok(resp, "Shop menu retrieved successfully", data);
    }
}