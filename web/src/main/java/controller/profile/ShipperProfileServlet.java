package controller.profile;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.user.ShipperDAO;
import model.user.Shipper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/me/shipper")
public class ShipperProfileServlet extends HttpServlet {
    private final ShipperDAO shipperDAO = new ShipperDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer shipperId = AuthGuard.requireShipperId(req, resp);
        if (shipperId == null) {
            return;
        }

        Shipper shipper = shipperDAO.getShipperByAccountId(shipperId);
        if (shipper == null) {
            JsonResponse.notFound(resp, "Shipper profile not found");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idShipper", shipper.getIdShipper());
        data.put("hoTen", shipper.getHoTen());
        data.put("ngaySinh", shipper.getNgaySinh());
        data.put("sdt", shipper.getSdt());

        JsonResponse.ok(resp, "Shipper profile retrieved successfully", data);
    }
}
