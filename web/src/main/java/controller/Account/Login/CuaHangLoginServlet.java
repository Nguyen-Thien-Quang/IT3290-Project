package controller.Account.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.user.CuaHangDAO;
import dao.user.TaiKhoanDAO;
import controller.Account.HashUtil;
import model.user.TaiKhoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.roles;

/**
 * Servlet handling login requests for Shop Manager (Cửa hàng) accounts.
 * This servlet processes POST requests containing email and password in JSON format.
 * It validates the credentials against the database, verifies the user's role,
 * and manages the session by storing both the account information and the specific shop ID.
 * 
 * <p>Request format:
 * <pre>
 * {
 *   "email": "shop@example.com",
 *   "password": "yourpassword"
 * }
 * </pre>
 * </p>
 */
@WebServlet("/api/shop/login")
public class CuaHangLoginServlet extends HttpServlet {
    /** Gson instance for JSON serialization and deserialization */
    private final Gson gson = new Gson();

    /**
     * Handles POST requests for shop manager login.
     * 
     * @param req  the HttpServletRequest object containing the login credentials in JSON format
     * @param resp the HttpServletResponse object used to return the login result as JSON
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON and encoding to UTF-8 to support Vietnamese characters
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Read the JSON request body from the input stream
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parse the JSON string into a JsonObject
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        } catch (Exception e) {
            // Return 400 Bad Request if the JSON payload is malformed
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("success", false);
            errorResp.put("message", "Invalid JSON format");
            resp.getWriter().write(gson.toJson(errorResp));
            return;
        }

        // Extract credentials from the JSON object with null checks
        String email = jsonObject.has("email") && !jsonObject.get("email").isJsonNull() ? jsonObject.get("email").getAsString() : null;
        String password = jsonObject.has("password") && !jsonObject.get("password").isJsonNull() ? jsonObject.get("password").getAsString() : null;

        Map<String, Object> responseMap = new HashMap<>();
        TaiKhoanDAO tkdao = new TaiKhoanDAO();
        HttpSession session = req.getSession();

        // Validate that both email and password were provided in the request
        if (email == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing email or password");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // 1. Verify if the email exists in the system to provide specific feedback
        if (!tkdao.checkEmailExist(email)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Email does not exist!");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // 2. Hash the input password using the SHA-256 utility for comparison
        String hashedPassword = HashUtil.hashPassword(password);

        // 3. Attempt to authenticate using the TaiKhoanDAO
        TaiKhoan user = tkdao.login(email, hashedPassword);
        if (user != null) {
            // 4. Role Authorization: Ensure the account has the correct "Cửa hàng" role
            if (!roles.SHOP.equals(user.getVaiTro())) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                responseMap.put("success", false);
                responseMap.put("message", "Access denied: This account is not a Shop Manager account!");
            } else {
                // Authentication successful: establish the user session
                session.setAttribute("user", user);

                // 5. Session Enrichment: Fetch and store the role-specific shopId
                // This allows other servlets to easily access the shop's primary key
                CuaHangDAO cdao = new CuaHangDAO();
                model.user.CuaHang c = cdao.getCuaHangByAccountId(user.getIdTaiKhoan());
                if (c != null) {
                    session.setAttribute("shopId", c.getIdCuaHang());
                }

                // Prepare successful response
                responseMap.put("success", true);
                responseMap.put("message", "Login successful");
                responseMap.put("role", user.getVaiTro());
                responseMap.put("email", user.getEmail());
            }
        } else {
            // 6. Authentication failure: Likely due to an incorrect password
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("success", false);
            responseMap.put("message", "Invalid password!");
        }
        
        // Finalize and send the JSON response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
