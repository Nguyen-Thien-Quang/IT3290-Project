package controller.profile;

import controller.utility.AuthGuard;
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
import java.util.Map;

@WebServlet("/api/me/shop")
public class ShopProfileServlet extends HttpServlet {
    private final CuaHangDAO cuaHangDAO = new CuaHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shopId = AuthGuard.requireShopId(req, resp);
        if (shopId == null) {
            return;
        }

        CuaHang shop = cuaHangDAO.getStoreProfileById(shopId);
        if (shop == null) {
            JsonResponse.notFound(resp, "Shop profile not found");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idCuaHang", shop.getIdCuaHang());
        data.put("tenCuaHang", shop.getTenCuaHang());
        data.put("diaChi", shop.getDiaChi());
        data.put("sdt", shop.getSdt());
        data.put("doanhThu", shop.getDoanhThu());

        JsonResponse.ok(resp, "Shop profile retrieved successfully", data);
    }
}
