package controller.Account.Register;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.user.TaiKhoanDAO;
import controller.Account.HashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * nhận vào body request dạng JSON bao gồm cá thông tin email, mật khẩu, ngày sinh,...
 * sau khi tạo tài khảon thành công thì trở lại về trang đăng nhập để đăng
 */
@WebServlet("/api/customer/register")
public class KhachHangRegisterServlet extends HttpServlet {
    private final Gson gson = new Gson();

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

        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("success", false);
            errorResp.put("message", "Invalid JSON format");
            resp.getWriter().write(gson.toJson(errorResp));
            return;
        }

        String email = jsonObject.has("email") && !jsonObject.get("email").isJsonNull() ? jsonObject.get("email").getAsString() : null;
        String password = jsonObject.has("password") && !jsonObject.get("password").isJsonNull() ? jsonObject.get("password").getAsString() : null;
        String name = jsonObject.has("name") && !jsonObject.get("name").isJsonNull() ? jsonObject.get("name").getAsString() : null;
        String birthdayStr = jsonObject.has("birthday") && !jsonObject.get("birthday").isJsonNull() ? jsonObject.get("birthday").getAsString() : null;
        String address = jsonObject.has("addr") && !jsonObject.get("addr").isJsonNull() ? jsonObject.get("addr").getAsString() : null;
        String SDT = jsonObject.has("SDT") && !jsonObject.get("SDT").isJsonNull() ? jsonObject.get("SDT").getAsString() : null;

        Date birthday = null;
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            try {
                birthday = Date.valueOf(birthdayStr);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> responseMap = new HashMap<>();

        if (email == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing email or password");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        TaiKhoanDAO tkdao = new TaiKhoanDAO();

        boolean success = tkdao.registerKhachHang(email, hashedPassword, name, birthday, address, SDT);
        
        if (success) {
            responseMap.put("success", true);
            responseMap.put("message", "Registration successful");
        } else {
            // Usually failure in this DAO method means email exists due to checkEmailExist call inside it
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            responseMap.put("success", false);
            responseMap.put("message", "Registration failed: Email might already exist or system error");
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}
