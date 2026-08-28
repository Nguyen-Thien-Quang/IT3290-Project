package controller.food;

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
import java.util.Map;

// return food detail by id
// ex: /api/foods/5
@WebServlet("/api/foods/*")
public class FoodDetailServlet extends HttpServlet {
    private final MonAnDAO monAnDAO = new MonAnDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Food ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int foodId;
        try {
            foodId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid food ID format");
            return;
        }

        MonAn food = monAnDAO.getById(foodId);
        if (food == null) {
            JsonResponse.notFound(resp, "Food not found");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("food", food);

        JsonResponse.ok(resp, "Food retrieved successfully", data);
    }
}
