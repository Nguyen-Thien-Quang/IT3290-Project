package controller.Account.Register;

import java.io.IOException;

import dao.user.TaiKhoanDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/customer/register")
public class KhachHangRegisterServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String birthday = req.getParameter("birthday");
        String address = req.getParameter("addr");
        String SDT = req.getParameter("SDT");

        TaiKhoanDAO tkdao = new TaiKhoanDAO();

        tkdao.createCustomerAccount(email, password, name, birthday, address, SDT);
        resp.sendRedirect("/login.html");
    }
}
