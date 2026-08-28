package controller.order;

import com.google.gson.Gson;

import controller.utility.AuthGuard;
import controller.utility.JsonResponse;
import dao.order.DonHangDAO;
import model.order.DonHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// handle both single order detail and order actions (accept, deliver, cancel) for authenticated users
// eg: GET /api/orders/1174 for order detail
// eg: POST /api/orders/125/accept for shipper to accept an order
// eg: POST /api/orders/125/cancel for customer to cancel an order
@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    // GET /api/orders/{id} — single order detail
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Order ID is required");
            return;
        }

        String idStr = pathInfo.substring(1);
        int donHangId;
        try {
            donHangId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp, "Invalid order ID format");
            return;
        }

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        Integer shopId = AuthGuard.requireShopId(req, resp);
        Integer shipperId = AuthGuard.requireShipperId(req, resp);

        DonHang order = null;

        if (customerId != null) {
            order = donHangDAO.getActiveOrdersByKhachHang(customerId).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
        } else if (shopId != null) {
            order = donHangDAO.getOrdersByStoreAndStatus(shopId, null).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
        } else if (shipperId != null) {
            order = donHangDAO.getDonHangsByShipper(shipperId).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
        } else {
            JsonResponse.unauthorized(resp, "Authentication required");
            return;
        }

        if (order == null) {
            JsonResponse.notFound(resp, "Order not found");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idDonHang", order.getIdDonHang());
        data.put("idGioHang", order.getIdGioHang());
        data.put("idKhachHang", order.getIdKhachHang());
        data.put("idShipper", order.getIdShipper());
        data.put("idVoucher", order.getIdVoucher());
        data.put("thoiGianDat", order.getThoiGianDat());
        data.put("trangThai", order.getTrangThai());
        data.put("tongTien", order.getTongTien());
        data.put("phuongThucThanhToan", order.getPhuongThucThanhToan());

        JsonResponse.ok(resp, "Order retrieved successfully", data);
    }

    // POST /api/orders/{id}/{action} — accept, deliver, cancel
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.badRequest(resp, "Order ID and action are required");
            return;
        }

        String action = null;
        if (pathInfo.endsWith("/accept")) {
            action = "accept";
        } else if (pathInfo.endsWith("/deliver")) {
            action = "deliver";
        } else if (pathInfo.endsWith("/cancel")) {
            action = "cancel";
        }

        if (action == null) {
            JsonResponse.badRequest(resp, "Invalid action. Use /accept, /deliver, or /cancel");
            return;
        }

        String idStr = pathInfo.substring(1);
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

        boolean success = false;
        String message = "";

        switch (action) {
            case "accept":
            case "deliver": {
                Integer shipperId = AuthGuard.requireShipperId(req, resp);
                if (shipperId == null) {
                    return;
                }
                if ("accept".equals(action)) {
                    success = donHangDAO.acceptOrder(donHangId, shipperId);
                    if (success) {
                        message = "Order accepted successfully";
                    } else {
                        JsonResponse.badRequest(resp, "Could not accept order. It may have already been taken or does not exist.");
                        return;
                    }
                } else {
                    success = donHangDAO.confirmDelivery(donHangId);
                    if (success) {
                        message = "Order marked as delivered";
                    } else {
                        JsonResponse.badRequest(resp, "Could not mark order as delivered");
                        return;
                    }
                }
                break;
            }
            case "cancel": {
                Integer customerId = AuthGuard.requireCustomerId(req, resp);
                if (customerId == null) {
                    return;
                }
                success = donHangDAO.cancelOrder(donHangId, customerId);
                if (success) {
                    message = "Order cancelled successfully";
                } else {
                    JsonResponse.badRequest(resp, "Could not cancel order. It may not exist, not belong to you, or is not in a cancellable state.");
                    return;
                }
                break;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", donHangId);
        data.put("action", action);

        JsonResponse.ok(resp, message, data);
    }
}
