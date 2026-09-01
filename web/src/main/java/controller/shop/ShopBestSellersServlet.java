package controller.shop;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.report.ThongKeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/me/shop/best-sellers")
public class ShopBestSellersServlet extends HttpServlet {
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        int k = 5;
        try {
            k = Integer.parseInt(req.getParameter("k"));
        } catch (NumberFormatException e) {
            k = 5;
        }

        List<Map<String, Object>> bestSellers = thongKeDAO.getBestSellerByRevenue(shopId, k);

        Map<String, Object> data = new HashMap<>();
        data.put("items", bestSellers);
        data.put("count", bestSellers.size());
        data.put("k", k);

        JsonResponse.ok(resp, "Best sellers retrieved successfully", data);
    }
}
