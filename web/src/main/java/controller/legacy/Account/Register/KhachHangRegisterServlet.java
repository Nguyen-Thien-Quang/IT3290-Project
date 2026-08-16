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
 * Servlet handling registration requests for Customer (Khách hàng) accounts.
 * This servlet processes POST requests containing user details in JSON format.
 * It hashes the password, validates the input, and persists the new customer account 
 * and profile using TaiKhoanDAO.
 * 
 * <p>Request format:
 * <pre>
 * {
 *   "email": "customer@example.com",
 *   "password": "yourpassword",
 *   "name": "Full Name",
 *   "birthday": "1990-01-01",
 *   "address": "123 Street, City",
 *   "SDT": "0123456789"
 * }
 * </pre>
 * </p>
 */
@WebServlet("/api/customer/register")
public class KhachHangRegisterServlet extends HttpServlet {
    /** Gson instance for JSON serialization and deserialization */
    private final Gson gson = new Gson();

    /**
     * Handles POST requests for customer registration.
     * 
     * @param req  the HttpServletRequest object containing the registration details in JSON format
     * @param resp the HttpServletResponse object used to return the registration result as JSON
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response type to JSON and encoding to UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Read the JSON request body
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("success", false);
            errorResp.put("message", "Invalid JSON format");
            resp.getWriter().write(gson.toJson(errorResp));
            return;
        }

        // Extract registration details from the JSON object
        String email = jsonObject.has("email") && !jsonObject.get("email").isJsonNull() ? jsonObject.get("email").getAsString() : null;
        String password = jsonObject.has("password") && !jsonObject.get("password").isJsonNull() ? jsonObject.get("password").getAsString() : null;
        String name = jsonObject.has("name") && !jsonObject.get("name").isJsonNull() ? jsonObject.get("name").getAsString() : null;
        String birthdayStr = jsonObject.has("birthday") && !jsonObject.get("birthday").isJsonNull() ? jsonObject.get("birthday").getAsString() : null;
        String address = jsonObject.has("address") && !jsonObject.get("address").isJsonNull() ? jsonObject.get("address").getAsString() : null;
        String SDT = jsonObject.has("SDT") && !jsonObject.get("SDT").isJsonNull() ? jsonObject.get("SDT").getAsString() : null;

        // Parse birthday string to java.sql.Date
        Date birthday = null;
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            try {
                birthday = Date.valueOf(birthdayStr);
            } catch (IllegalArgumentException e) {
                // Log the error but continue; the DAO might handle null or we could return an error
                e.printStackTrace();
            }
        }

        Map<String, Object> responseMap = new HashMap<>();

        // Basic validation for mandatory fields
        if (email == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("success", false);
            responseMap.put("message", "Missing email or password");
            resp.getWriter().write(gson.toJson(responseMap));
            return;
        }

        // Hash the password before storage
        String hashedPassword = HashUtil.hashPassword(password);
        TaiKhoanDAO tkdao = new TaiKhoanDAO();

        // Attempt to register the new customer
        boolean success = tkdao.registerKhachHang(email, hashedPassword, name, birthday, address, SDT);
        
        if (success) {
            responseMap.put("success", true);
            responseMap.put("message", "Registration successful");
            // Include redirect URL for the frontend to navigate to the login page
            responseMap.put("redirect", req.getContextPath() + "/login.html");
        } else {
            // Failure usually indicates the email already exists or a database error
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            responseMap.put("success", false);
            responseMap.put("message", "Registration failed: Email might already exist or system error");
        }

        // Send the response
        resp.getWriter().write(gson.toJson(responseMap));
    }
}
