package controller.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.utility.HashUtil;
import controller.utility.JsonResponse;
import dao.user.TaiKhoanDAO;
import model.user.roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/api/auth/register")
public class RegisterServlet extends HttpServlet {
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
        String role = json.has("role") && !json.get("role").isJsonNull() ? json.get("role").getAsString() : null;

        if (email == null || password == null || role == null) {
            JsonResponse.badRequest(resp, "Email, password, and role are required");
            return;
        }

        String dbRole;
        switch (role) {
            case "customer":
                dbRole = roles.CUSTOMER;
                break;
            case "store":
                dbRole = roles.SHOP;
                break;
            case "shipper":
                dbRole = roles.SHIPPER;
                break;
            default:
                JsonResponse.badRequest(resp, "Invalid role. Must be: customer, store, or shipper");
                return;
        }

        String name = json.has("name") && !json.get("name").isJsonNull() ? json.get("name").getAsString() : null;
        String birthdayStr = json.has("birthday") && !json.get("birthday").isJsonNull() ? json.get("birthday").getAsString() : null;
        String address = json.has("address") && !json.get("address").isJsonNull() ? json.get("address").getAsString() : null;
        String sdt = json.has("sdt") && !json.get("sdt").isJsonNull() ? json.get("sdt").getAsString() : null;

        Date birthday = null;
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            try {
                birthday = Date.valueOf(birthdayStr);
            } catch (IllegalArgumentException e) {
                JsonResponse.badRequest(resp, "Invalid birthday format. Use YYYY-MM-DD");
                return;
            }
        }

        String hashedPassword = HashUtil.hashPassword(password);
        boolean success = false;

        try {
            switch (dbRole) {
                case roles.CUSTOMER:
                    success = taiKhoanDAO.registerKhachHang(email, hashedPassword, name, birthday, address, sdt);
                    break;
                case roles.SHOP:
                    success = taiKhoanDAO.registerCuaHang(email, hashedPassword, name, address, sdt);
                    break;
                case roles.SHIPPER:
                    success = taiKhoanDAO.registerShipper(email, hashedPassword, name, birthday, sdt);
                    break;
            }
        } catch (Exception e) {
            JsonResponse.internalError(resp, e);
            return;
        }

        if (success) {
            JsonResponse.created(resp, "Registration successful", null);
        } else {
            JsonResponse.conflict(resp, "Registration failed. Email may already exist.");
        }
    }
}