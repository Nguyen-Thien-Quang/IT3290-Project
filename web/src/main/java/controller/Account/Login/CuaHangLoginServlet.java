package main.java.controller.Account.Login;

import java.io.IOException;

import dao.user.TaiKhoanDAO;
import model.user.TaiKhoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/shop/login")
public class CuaHangLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        TaiKhoanDAO tkdao = new TaiKhoanDAO();
        HttpSession session = req.getSession();

        // use CheckEmailExist from DAO to check whether there is account
        if (!tkdao.checkEmailExist(email)) {
            resp.getWriter().write("Email does not exist!");
            return;
        }

        // then login
        TaiKhoan user = tkdao.login(email, password);
        if (user != null) {
            // check Is the role of this account really Cửa hàng
            if (!"Cửa hàng".equals(user.getVaiTro())) {
                resp.getWriter().write("Access denied: This account is not a shop manager account!");
                return;
            }
            // use HttpSession from Servlet
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            resp.getWriter().write("Invalid password!");
        }
    }
}
