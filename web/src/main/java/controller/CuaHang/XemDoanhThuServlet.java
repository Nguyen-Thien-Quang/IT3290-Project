package controller.CuaHang;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
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
import model.user.roles;

/**
 * Servlet handling requests to view shop revenue statistics.
 * This servlet processes GET requests with date range parameters.
 * It verifies the shop manager's session and retrieves data using ThongKeDAO.
 */
@WebServlet("/api/shop/sales")
public class XemDoanhThuServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();

    /**
     * Handles GET requests to retrieve revenue statistics.
     * 
     * @param req  the HttpServletRequest object containing startDate and endDate parameters
     * @param resp the HttpServletResponse object used to return the revenue data as JSON
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
        Integer shopId = (Integer) session.getAttribute("shopId");

        Map<String, Object> responseMap = new HashMap<>();

        // 1. Check if user is logged in and has the correct role
        if (user == null || !roles.SHOP.equals(user.getVaiTro()) || shopId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Unauthorized: Please login as a Shop Manager");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // 2. Get startDate and endDate from query parameters
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        // Basic validation: if dates are missing, we can provide a default or return an error
        if (startDateStr == null || endDateStr == null || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing startDate or endDate parameter (format: YYYY-MM-DD)");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        try {
            // 3. Parse parameters to java.sql.Date
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);

            // 4. Retrieve revenue statistics using ThongKeDAO
            ThongKeDAO tkDAO = new ThongKeDAO();
            float totalRevenue = tkDAO.getDoanhThuTheoGiaiDoan(shopId, startDate, endDate);

            // 5. Prepare and send successful response
            responseMap.put("success", true);
            responseMap.put("shopId", shopId);
            responseMap.put("startDate", startDateStr);
            responseMap.put("endDate", endDateStr);
            responseMap.put("totalRevenue", totalRevenue);
            
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Invalid date format. Use YYYY-MM-DD.");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "System error: " + e.getMessage());
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}
