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
import java.util.List;
import java.util.Map;

// find list of food by keyword
// ex: /api/foods?keyword=pho&page=0&size=20
@WebServlet("/api/foods")
public class FoodListServlet extends HttpServlet {
    private final MonAnDAO monAnDAO = new MonAnDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        keyword = keyword.trim();

        int page = 0;
        int size = 20;
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 0;
        }
        try {
            size = Integer.parseInt(req.getParameter("size"));
        } catch (NumberFormatException e) {
            size = 20;
        }
        if (size > 100) {
            size = 100;
        }

        List<MonAn> list = monAnDAO.searchByName(keyword);

        Map<String, Object> data = new HashMap<>();
        data.put("items", list);
        data.put("page", page);
        data.put("size", size);
        data.put("total", list.size());

        JsonResponse.ok(resp, "Foods retrieved successfully", data);
    }
}
