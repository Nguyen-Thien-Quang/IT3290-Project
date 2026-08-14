package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    public Connection getConnection() throws Exception {
        // Added trustServerCertificate=true
        String url = "jdbc:sqlserver://db:1433;databaseName=FoodProject;encrypt=false;trustServerCertificate=true;";
        String user = "sa";
        String pass = "Thang123"; // Make sure this matches the password set in your docker container
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, pass);
    }
}
