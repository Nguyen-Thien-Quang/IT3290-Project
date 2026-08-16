package controller.common;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for sending standardized JSON responses.
 * <p>
 * All API endpoints should use this class to ensure consistent response format:
 * <pre>
 * {
 *   "success": true|false,
 *   "message": "Human-readable description",
 *   "data": {...} | [...] | null
 * }
 * </pre>
 * <p>
 * Messages are in English for API consumers. Vietnamese strings from the database
 * (e.g., order status, food status) are passed through as-is in the data payload.
 */
public class JsonResponse {
    private static final Gson gson = new Gson();

    private JsonResponse() {}

    /**
     * Core method to write a JSON envelope to the HTTP response.
     *
     * @param resp       servlet response object
     * @param status     HTTP status code (200, 201, 400, 401, 403, 404, 409, 500)
     * @param success    true for success responses, false for errors
     * @param message    human-readable message in English
     * @param data       payload object (can be null, Map, List, POJO)
     * @throws IOException if writing to response fails
     */
    public static void send(HttpServletResponse resp, int status, boolean success, String message, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> envelope = new HashMap<>(3);
        envelope.put("success", success);
        envelope.put("message", message);
        if (data != null) {
            envelope.put("data", data);
        }
        resp.getWriter().write(gson.toJson(envelope));
    }

    /**
     * 200 OK — successful response with payload.
     */
    public static void ok(HttpServletResponse resp, String message, Object data) throws IOException {
        send(resp, HttpServletResponse.SC_OK, true, message, data);
    }

    /**
     * 201 Created — resource created successfully.
     */
    public static void created(HttpServletResponse resp, String message, Object data) throws IOException {
        send(resp, HttpServletResponse.SC_CREATED, true, message, data);
    }

    /**
     * 400 Bad Request — client sent invalid/malformed input.
     */
    public static void badRequest(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_BAD_REQUEST, false, message, null);
    }

    /**
     * 401 Unauthorized — no valid session / not logged in.
     */
    public static void unauthorized(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_UNAUTHORIZED, false, message, null);
    }

    /**
     * 403 Forbidden — authenticated but wrong role / insufficient permission.
     */
    public static void forbidden(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_FORBIDDEN, false, message, null);
    }

    /**
     * 404 Not Found — resource does not exist.
     */
    public static void notFound(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_NOT_FOUND, false, message, null);
    }

    /**
     * 409 Conflict — resource conflict (e.g., duplicate email on register).
     */
    public static void conflict(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_CONFLICT, false, message, null);
    }

    /**
     * 500 Internal Server Error — unexpected server-side failure.
     */
    public static void internalError(HttpServletResponse resp, String message) throws IOException {
        send(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, message, null);
    }

    /**
     * 500 Internal Server Error — wraps an exception message.
     * Use sparingly; prefer explicit error messages to avoid leaking stack traces.
     */
    public static void internalError(HttpServletResponse resp, Exception e) throws IOException {
        internalError(resp, "Server error: " + e.getMessage());
    }
}