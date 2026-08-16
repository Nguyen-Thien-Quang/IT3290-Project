package controller.utility;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password hashing utility using BCrypt.
 * <p>
 * Replaces the legacy SHA-256 implementation. BCrypt includes a salt
 * and is computationally expensive by design, making it suitable
 * for password storage.
 * <p>
 * Usage:
 * <pre>
 * String hash = HashUtil.hashPassword("plainPassword");
 * boolean matches = HashUtil.checkPassword("plainPassword", hash);
 * </pre>
 */
public class HashUtil {

    private static final int BCRYPT_COST = 12;

    private HashUtil() {}

    /**
     * Hashes a plain-text password using BCrypt with a generated salt.
     *
     * @param plainPassword the raw password
     * @return BCrypt hash string (includes salt and cost factor)
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_COST));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainPassword the raw password to check
     * @param hashedPassword the stored BCrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }
}