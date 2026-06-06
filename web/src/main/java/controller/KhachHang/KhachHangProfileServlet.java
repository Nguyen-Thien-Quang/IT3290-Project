package controller.KhachHang;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import dao.user.KhachHangDAO;
import model.user.KhachHang;
import model.user.TaiKhoan;

@WebServlet("/api/customer/profile")
public class KhachHangProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        TaiKhoan userAccount = (TaiKhoan) session.getAttribute("user");

        if (userAccount == null || !"Khách hàng".equals(userAccount.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized or not a customer account\"}");
            return;
        }

        KhachHangDAO dao = new KhachHangDAO();
        KhachHang khInfo = dao.getProfileById(userAccount.getIdTaiKhoan());

        if (khInfo != null) {
            Gson gson = new Gson();
            String json = gson.toJson(khInfo);
            resp.getWriter().write(json);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Customer details not found\"}");
        }
    }
}
