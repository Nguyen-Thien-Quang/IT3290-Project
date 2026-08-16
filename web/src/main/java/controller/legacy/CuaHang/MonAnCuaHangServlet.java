package controller.CuaHang;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import dao.food.MonAnDAO;
import model.food.MonAn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet handling requests to retrieve all food items belonging to a specific shop.
 * This implementation strictly requires a shop ID provided via the 'id' query parameter.
 */
@WebServlet("/api/shop/foods")
public class MonAnCuaHangServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();

    /**
     * Handles GET requests to fetch a shop's menu items.
     * The 'id' query parameter is mandatory.
     * 
     * @param req  the HttpServletRequest containing the mandatory 'id' parameter
     * @param resp the HttpServletResponse used to return the food list as JSON
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response headers for JSON and UTF-8 encoding
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1. Retrieve the store ID from the request parameter 'id'
        String idParam = req.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            // Respond with an error if the mandatory 'id' parameter is missing
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\": false, \"message\": \"Missing mandatory 'id' parameter.\"}");
            return;
        }

        int shopId;
        try {
            shopId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            // Respond with an error if the 'id' parameter is not a valid integer
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\": false, \"message\": \"Invalid shop ID format.\"}");
            return;
        }

        // 2. Fetch the food items using the specified getByStore() method from MonAnDAO
        MonAnDAO dao = new MonAnDAO();
        List<MonAn> foods = dao.getByStore(shopId);
        
        // 3. Return the list of food items as a JSON array
        resp.getWriter().write(gson.toJson(foods));
    }
}
