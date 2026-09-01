package controller.shop;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.JsonResponse;
import dao.user.CuaHangDAO;
import model.user.CuaHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/shops")
public class ShopListServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final CuaHangDAO cuaHangDAO = new CuaHangDAO();

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

        List<CuaHang> list = cuaHangDAO.searchCuaHangByKeyword(keyword);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("items", list);
        pageInfo.put("page", page);
        pageInfo.put("size", size);
        pageInfo.put("total", list.size());
        data.put("shops", pageInfo);

        JsonResponse.ok(resp, "Shops retrieved successfully", data);
    }
}