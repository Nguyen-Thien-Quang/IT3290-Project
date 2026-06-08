package controller.CuaHang;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import dao.report.ThongKeDAO;
import model.user.TaiKhoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet handling requests for the best-selling food items by revenue of a shop.
 * Returns the top K items as a JSON list.
 */
@WebServlet("/api/shop/food/bestSeller")
public class MonAnBanChayNhatServlet extends HttpServlet {
    private final Gson gson = new Gson();

    /**
     * Handles GET requests to retrieve best-selling food items.
     * 
     * @param req  the HttpServletRequest containing the parameter 'k'
     * @param resp the HttpServletResponse used to return the JSON result
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        Integer shopId = (Integer) session.getAttribute("shopId");

        // 1. Authorization Check: Must be logged in as a Shop Manager
        if (user == null || shopId == null || !"Cửa hàng".equals(user.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"success\": false, \"message\": \"Unauthorized: Please log in as a Shop Manager.\"}");
            return;
        }

        // 2. Parse Parameter 'k': Number of top items to retrieve
        String kStr = req.getParameter("k");
        int k = 5; // Default value
        if (kStr != null) {
            try {
                k = Integer.parseInt(kStr);
            } catch (NumberFormatException e) {
                // Keep default value if parsing fails
            }
        }

        // 3. Data Retrieval via DAO
        ThongKeDAO thongKeDAO = new ThongKeDAO();
        List<Map<String, Object>> bestSellers = thongKeDAO.getBestSellerByRevenue(shopId, k);

        // 4. Return JSON Response
        if (bestSellers != null) {
            resp.getWriter().write(gson.toJson(bestSellers));
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\": false, \"message\": \"Failed to retrieve best-selling items.\"}");
        }
    }
}
