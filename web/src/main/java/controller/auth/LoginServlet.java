package controller.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.HashUtil;
import controller.utility.JsonResponse;
import dao.user.KhachHangDAO;
import dao.user.CuaHangDAO;
import dao.user.ShipperDAO;
import dao.user.TaiKhoanDAO;
import model.user.TaiKhoan;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/login")
public class LoginServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject json;
        try {
            json = gson.fromJson(sb.toString(), JsonObject.class);
        } catch (Exception e) {
            JsonResponse.badRequest(resp, "Invalid JSON format");
            return;
        }

        String email = json.has("email") && !json.get("email").isJsonNull() ? json.get("email").getAsString() : null;
        String password = json.has("password") && !json.get("password").isJsonNull() ? json.get("password").getAsString() : null;

        if (email == null || password == null) {
            JsonResponse.badRequest(resp, "Email and password are required");
            return;
        }

        TaiKhoan user = taiKhoanDAO.getByEmail(email);

        if (user == null || !HashUtil.checkPassword(password, user.getMatKhau())) {
            JsonResponse.unauthorized(resp, "Invalid credentials");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        Integer roleSpecificId = null;
        String roleKey = null;

        if (roles.CUSTOMER.equals(user.getVaiTro())) {
            KhachHangDAO khDao = new KhachHangDAO();
            var kh = khDao.getByAccountId(user.getIdTaiKhoan());
            if (kh != null) {
                roleSpecificId = kh.getIdKhachHang();
                session.setAttribute("customerId", roleSpecificId);
                roleKey = "customer";
            }
        } else if (roles.SHOP.equals(user.getVaiTro())) {
            CuaHangDAO chDao = new CuaHangDAO();
            var ch = chDao.getCuaHangByAccountId(user.getIdTaiKhoan());
            if (ch != null) {
                roleSpecificId = ch.getIdCuaHang();
                session.setAttribute("shopId", roleSpecificId);
                roleKey = "store";
            }
        } else if (roles.SHIPPER.equals(user.getVaiTro())) {
            ShipperDAO spDao = new ShipperDAO();
            var sp = spDao.getShipperByAccountId(user.getIdTaiKhoan());
            if (sp != null) {
                roleSpecificId = sp.getIdShipper();
                session.setAttribute("shipperId", roleSpecificId);
                roleKey = "shipper";
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("role", user.getVaiTro());
        data.put("roleKey", roleKey);
        data.put("email", user.getEmail());
        data.put("id", user.getIdTaiKhoan());

        JsonResponse.ok(resp, "Login successful", data);
    }
}