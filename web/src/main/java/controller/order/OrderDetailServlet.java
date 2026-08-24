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
public class OrderDetailServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Integer customerId = AuthGuard.requireCustomerId(req, resp);
        Integer shopId = AuthGuard.requireShopId(req, resp);
        Integer shipperId = AuthGuard.requireShipperId(req, resp);

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

        DonHang order = null;
        String role = null;

        if (customerId != null) {
            // Customer: verify order belongs to this customer
            order = donHangDAO.getActiveOrdersByKhachHang(customerId).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
            role = "customer";
        } else if (shopId != null) {
            // Shop: verify order belongs to this store
            order = donHangDAO.getOrdersByStoreAndStatus(shopId, null).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
            role = "shop";
        } else if (shipperId != null) {
            // Shipper: verify order is assigned to this shipper
            order = donHangDAO.getDonHangsByShipper(shipperId).stream()
                    .filter(o -> o.getIdDonHang() == donHangId)
                    .findFirst()
                    .orElse(null);
            role = "shipper";
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

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order retrieved successfully");
        response.put("data", data);

        JsonResponse.ok(resp, "Order retrieved successfully", response);
    }
}