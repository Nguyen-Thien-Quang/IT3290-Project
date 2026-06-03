package main.java.controller.CuaHang;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import dao.user.CuaHangDAO;
import model.user.CuaHang;
import model.user.TaiKhoan;

@WebServlet("/api/shop/profile")
public class CuaHangProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1. Get the session and the user model stored during login
        HttpSession session = req.getSession();
        TaiKhoan userAccount = (TaiKhoan) session.getAttribute("user");

        // 2. Safety check: Is the user logged in and are they a Shop Manager?
        if (userAccount == null || !"Cửa hàng".equals(userAccount.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized or not a shop account\"}");
            return;
        }

        // 3. Use the ID from the session model to get full CuaHang info
        CuaHangDAO dao = new CuaHangDAO();
        CuaHang shopInfo = dao.getByAccountId(userAccount.getIdTaiKhoan());

        if (shopInfo != null) {
            // 4. Return the full shop model as JSON
            Gson gson = new Gson();
            String json = gson.toJson(shopInfo);
            resp.getWriter().write(json);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Shop details not found\"}");
        }
    }
}
