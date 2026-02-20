package api.database;

import api.config.Configuration;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    private static final String URL_DB = config.urlDB();
    private static final String USERNAME_DB = config.usernameDB();
    private static final String PASSWORD_DB = config.passwordDB();

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL_DB, USERNAME_DB, PASSWORD_DB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
