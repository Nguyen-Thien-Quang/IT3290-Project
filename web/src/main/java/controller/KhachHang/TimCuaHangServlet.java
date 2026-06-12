package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dao.user.CuaHangDAO;
import model.user.CuaHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet handling shop search requests for customers.
 * This servlet processes GET requests with a search keyword.
 * It retrieves a list of shops whose names contain the keyword using CuaHangDAO.
 */
@WebServlet("/api/shop/search")
public class TimCuaHangServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();
    /** DAO for shop-related database operations */
    private final CuaHangDAO cuaHangDAO = new CuaHangDAO();

    /**
     * Handles GET requests to search for shops by keyword.
     * 
     * @param req  the HttpServletRequest object containing the 'keyword' query parameter
     * @param resp the HttpServletResponse object used to return the matching shops as JSON
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON and encoding to UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Get search keyword from query parameter
        String keyword = req.getParameter("keyword");
        if (keyword == null) {
            keyword = ""; // Default to empty string to search for all shops or handle as empty list
        }
        keyword = keyword.trim();

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Search for shops using the keyword
            List<CuaHang> searchResults = cuaHangDAO.searchCuaHangByKeyword(keyword);

            // Prepare successful response
            responseMap.put("success", true);
            responseMap.put("keyword", keyword);
            responseMap.put("data", searchResults);
            responseMap.put("count", searchResults.size());
            
        } catch (Exception e) {
            // Handle unexpected database or processing errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "An error occurred during shop search: " + e.getMessage());
            e.printStackTrace();
        }

        // Send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
