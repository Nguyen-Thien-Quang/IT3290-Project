package controller.Shipper;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import dao.user.ShipperDAO;
import model.user.Shipper;
import model.user.TaiKhoan;

@WebServlet("/api/shipper/profile")
public class ShipperProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // set response type
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // get user account from current session
        HttpSession session = req.getSession();
        TaiKhoan userAccount = (TaiKhoan) session.getAttribute("user");

        // check role of account
        if (userAccount == null || !"Shipper".equals(userAccount.getVaiTro())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized or not a shipper account\"}");
            return;
        }

        // create new DAO object for query
        ShipperDAO dao = new ShipperDAO();
        Shipper shipperInfo = dao.getShipperByAccountId(userAccount.getIdTaiKhoan());

        // Jsonify data and put it in the response
        if (shipperInfo != null) {
            Gson gson = new Gson();
            String json = gson.toJson(shipperInfo);
            resp.getWriter().write(json);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Shipper details not found\"}");
        }
    }
}
