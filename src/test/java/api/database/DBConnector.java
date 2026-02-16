package api.database;

import api.config.Configuration;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                    config.urlDB(),
                    config.usernameDB(),
                    config.passwordDB());
    }
}
