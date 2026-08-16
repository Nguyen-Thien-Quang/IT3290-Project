package controller.MonAn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dao.food.MonAnDAO;
import model.food.MonAn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet handling food search requests.
 * This servlet processes GET requests with a search keyword.
 * It retrieves a list of foods whose names contain the keyword using MonAnDAO.
 */
@WebServlet("/api/food/search")
public class TimMonAnServlet extends HttpServlet {
    /** Gson instance for JSON serialization */
    private final Gson gson = new Gson();
    /** DAO for food-related database operations */
    private final MonAnDAO monAnDAO = new MonAnDAO();

    /**
     * Handles GET requests to search for foods by keyword.
     * 
     * @param req  the HttpServletRequest object containing the 'keyword' query parameter
     * @param resp the HttpServletResponse object used to return the matching foods as JSON
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
            keyword = ""; // Default to empty string to search for all available foods
        }
        keyword = keyword.trim();

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Search for foods using the keyword
            List<MonAn> searchResults = monAnDAO.searchByName(keyword);

            // Prepare successful response
            responseMap.put("success", true);
            responseMap.put("keyword", keyword);
            responseMap.put("data", searchResults);
            responseMap.put("count", searchResults.size());
            
        } catch (Exception e) {
            // Handle unexpected database or processing errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("success", false);
            responseMap.put("message", "An error occurred during food search: " + e.getMessage());
            e.printStackTrace();
        }

        // Send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
