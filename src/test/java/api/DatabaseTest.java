package api;

import api.DTO.Register;
import api.dataFactory.UserDataGenerator;
import api.database.DBConnector;
import api.database.DBUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private static Stream<List<Register>> accountsListProvider() {
        return Stream.of(UserDataGenerator.generateAccountsList(10));
    }

    @BeforeEach
    public void databaseSetup() {

        try (Connection connection = DBConnector.getConnection()) {
            DBUtilities.dropRegisterTable(connection);
            DBUtilities.createRegisterTable(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("accountsListProvider")
    public void checkIfAccountsExistInDatabaseTest(List<Register> expectedAccounts) throws SQLException {

        String sql = "select email, password from register";

        try(Connection connection = DBConnector.getConnection()) {

            DBUtilities.addListOfAccounts(expectedAccounts, connection);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Register> actualAccounts = new ArrayList<>();

            while (resultSet.next()) {
                actualAccounts.add(new Register(
                        resultSet.getString("email"),
                        resultSet.getString("password"))
                );
            }
            assertEquals(expectedAccounts.size(), actualAccounts.size());
            assertEquals(expectedAccounts, actualAccounts);
        }
    }
}
