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
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/me/shop/revenue")
public class ShopRevenueServlet extends HttpServlet {
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
            JsonResponse.badRequest(resp, "Missing startDate or endDate parameter (format: YYYY-MM-DD)");
            return;
        }

        Date startDate;
        Date endDate;
        try {
            startDate = Date.valueOf(startDateStr);
            endDate = Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            JsonResponse.badRequest(resp, "Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        float totalRevenue = thongKeDAO.getDoanhThuTheoGiaiDoan(shopId, startDate, endDate);

        Map<String, Object> data = new HashMap<>();
        data.put("startDate", startDateStr);
        data.put("endDate", endDateStr);
        data.put("totalRevenue", totalRevenue);

        JsonResponse.ok(resp, "Revenue retrieved successfully", data);
    }
}
