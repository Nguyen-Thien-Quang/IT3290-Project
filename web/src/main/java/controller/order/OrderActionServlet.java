package controller.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.DonHangDAO;
import model.order.DonHang;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/orders/*")
public class OrderActionServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Order ID is required");
            return;
        }

        // Determine action from path: /accept or /deliver
        String action = null;
        if (pathInfo.endsWith("/accept")) {
            action = "accept";
        } else if (pathInfo.endsWith("/deliver")) {
            action = "deliver";
        }

        if (action == null) {
            JsonResponse.badRequest(resp, "Invalid action. Use /accept or /deliver");
            return;
        }

        // Extract order ID from path: /api/orders/123/accept -> id = 123
        String idStr = pathInfo.substring(1); // remove leading /
        int slashIndex = idStr.indexOf('/');
        if (slashIndex > 0) {
            idStr = idStr.substring(0, slashIndex);
        }
        int donHangId;
        try {
            donHangId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid order ID format");
            return;
        }

        Integer shipperId = AuthGuard.requireShipperId(req, resp);
        if (shipperId == null) {
            return;
        }

        boolean success = false;
        String message = "";

        switch (action) {
            case "accept":
                success = donHangDAO.acceptOrder(donHangId, shipperId);
                if (success) {
                    message = "Order accepted successfully. Status changed to 'Đang giao'";
                } else {
                    JsonResponse.badRequest(resp, "Could not accept order. It may have already been taken or does not exist.");
                    return;
                }
                break;
            case "deliver":
                success = donHangDAO.confirmDelivery(donHangId);
                if (success) {
                    message = "Order marked as delivered";
                } else {
                    JsonResponse.badRequest(resp, "Could not mark order as delivered");
                    return;
                }
                break;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", donHangId);
        data.put("action", action);
        data.put("success", success);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);

        JsonResponse.ok(resp, message, data);
    }
}