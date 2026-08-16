package controller.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.TaiKhoan;
import model.user.roles;

import java.io.IOException;

/**
 * Centralized authentication and authorization guards for servlet endpoints.
 * <p>
 * All servlets should delegate session/role checks to this class to ensure:
 * <ul>
 *   <li>Consistent HTTP status codes: 401 for missing/invalid session, 403 for wrong role</li>
 *   <li>Consistent error messages in English</li>
 *   <li>Single place to change session attribute names or role logic</li>
 * </ul>
 * <p>
 * Role constants are sourced from {@link model.user.roles} which uses Vietnamese
 * strings (e.g., "Khách hàng", "Cửa hàng", "Shipper") to match the database.
 * This class treats them as opaque identifiers.
 */
public class AuthGuard {

    private AuthGuard() {}

    /**
     * Verifies that the request has an active session with a logged-in user.
     *
     * @param req  servlet request
     * @param resp servlet response (error written directly if check fails)
     * @return true if authenticated, false if unauthorized (response already sent)
     * @throws IOException if writing error response fails
     */
    public static boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            JsonResponse.unauthorized(resp, "Authentication required");
            return false;
        }
        return true;
    }

    /**
     * Verifies that the current user has one of the allowed roles.
     * Must be called after {@link #requireLogin} (which this method invokes).
     *
     * @param req          servlet request
     * @param resp         servlet response (error written directly if check fails)
     * @param allowedRoles one or more role strings from {@link model.user.roles}
     * @return true if authorized, false if forbidden (response already sent)
     * @throws IOException if writing error response fails
     */
    public static boolean requireRole(HttpServletRequest req, HttpServletResponse resp, String... allowedRoles) throws IOException {
        if (!requireLogin(req, resp)) return false;         // I thought this method must be called after requireLogin, but we can double-check here for safety

        HttpSession session = req.getSession(false);
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        String userRole = user.getVaiTro();

        for (String role : allowedRoles) {
            if (role.equals(userRole)) {
                return true;
            }
        }

        JsonResponse.forbidden(resp, "Access denied: required role(s) " + String.join(", ", allowedRoles));
        return false;
    }

    /**
     * Retrieves the customerId from session after verifying CUSTOMER role.
     *
     * @param req  servlet request
     * @param resp servlet response
     * @return customerId if present, null if missing (error response already sent)
     * @throws IOException if writing error response fails
     */
    public static Integer requireCustomerId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireRole(req, resp, roles.CUSTOMER)) return null;

        HttpSession session = req.getSession(false);
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            JsonResponse.internalError(resp, "Customer ID not found in session");
            return null;
        }
        return customerId;
    }

    /**
     * Retrieves the shopId from session after verifying SHOP role.
     *
     * @param req  servlet request
     * @param resp servlet response
     * @return shopId if present, null if missing (error response already sent)
     * @throws IOException if writing error response fails
     */
    public static Integer requireShopId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireRole(req, resp, roles.SHOP)) return null;

        HttpSession session = req.getSession(false);
        Integer shopId = (Integer) session.getAttribute("shopId");
        if (shopId == null) {
            JsonResponse.internalError(resp, "Shop ID not found in session");
            return null;
        }
        return shopId;
    }

    /**
     * Retrieves the shipperId from session after verifying SHIPPER role.
     *
     * @param req  servlet request
     * @param resp servlet response
     * @return shipperId if present, null if missing (error response already sent)
     * @throws IOException if writing error response fails
     */
    public static Integer requireShipperId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireRole(req, resp, roles.SHIPPER)) return null;

        HttpSession session = req.getSession(false);
        Integer shipperId = (Integer) session.getAttribute("shipperId");
        if (shipperId == null) {
            JsonResponse.internalError(resp, "Shipper ID not found in session");
            return null;
        }
        return shipperId;
    }

    /**
     * Gets the current logged-in user without any authorization checks.
     * Returns null if no session or no user attribute.
     *
     * @param req servlet request
     * @return TaiKhoan object or null
     */
    public static TaiKhoan getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        return (TaiKhoan) session.getAttribute("user");
    }
}