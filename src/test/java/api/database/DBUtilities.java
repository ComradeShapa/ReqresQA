package api.database;

import api.DTO.Register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBUtilities {

    private static final String DROP_REGISTER_TABLE = "drop table if exists register";
    private static final String CREATE_REGISTER_TABLE =
            "create table if not exists register(" +
                    "id serial primary key, " +
                    "email varchar(100) not null, " +
                    "password varchar(100) not null)";
    private static final String INSERT_INTO_REGISTER_EMAIL_PASSWORD_VALUES = "insert into register(email, password) values(?, ?)";
    private static final String SELECT_FROM_REGISTER_WHERE_EMAIL = "select * from register where email = ?";
    private static final String UPDATE_REGISTER_SET_PASSWORD_WHERE_EMAIL = "update register set password = ? where email = ?";
    private static final String DELETE_FROM_REGISTER_WHERE_EMAIL = "delete from register where email = ?";
    private static final String SELECT_FROM_REGISTER_WHERE_ID = "select * from register where id = ?";


    public static void createRegisterTable() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_REGISTER_TABLE)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropRegisterTable() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DROP_REGISTER_TABLE)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Register selectRegisterByEmail(String email) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_FROM_REGISTER_WHERE_EMAIL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Register register = new Register();
                    register.setEmail(resultSet.getString("email"));
                    register.setPassword(resultSet.getString("password"));
                    return register;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя с почтой: " + email, e);
        }
    }

    public static Register selectRegisterById(int id) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_FROM_REGISTER_WHERE_ID)) {
            int databaseId = ++id;
            statement.setInt(1, databaseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Register register = new Register();
                    register.setEmail(resultSet.getString("email"));
                    register.setPassword(resultSet.getString("password"));
                    return register;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя с ID: " + id, e);
        }
    }

    public static void postRegister(Register account) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_REGISTER_EMAIL_PASSWORD_VALUES)) {
            statement.setString(1, account.getEmail());
            statement.setString(2, account.getPassword());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void postRegister(List<Register> generatedAccountsList) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_REGISTER_EMAIL_PASSWORD_VALUES)) {

            for (Register account : generatedAccountsList) {
                statement.setString(1, account.getEmail());
                statement.setString(2, account.getPassword());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void patchRegister(String actualEmail, String expectedPassword) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTER_SET_PASSWORD_WHERE_EMAIL)) {

            statement.setString(1, expectedPassword);
            statement.setString(2, actualEmail);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пароля для: " + actualEmail, e);
        }
    }

    public static void deleteRegisterByEmail(String email) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FROM_REGISTER_WHERE_EMAIL)) {

            statement.setString(1, email);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении записи из БД по почте: " + email, e);
        }
    }
}
