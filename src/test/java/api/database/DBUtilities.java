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
                    "email varchar(50) not null check ( " +                         // от 1 до 50 символов
                    "length(email) between 1 and 50 and " +                         // без кириллицы, содержит ровно один "@"
                    "email ~ '^[A-Za-z0-9._-]+@[A-Za-z0-9._-]+$'), " +              // разрешены цифры и только указанные спец. символы
                    "password varchar(50) not null check ( " +
                    "length(password) between 1 and 50 " +                          // от 1 до 50 символов
                    "and password ~ '[0-9]' " +                                     // хотя бы одна цифра
//                            "and password ~ '[!@#$%^&*()_,.?\\\\\":{}|<>]' " +    // и хотя бы один спецсимвол
                    "and password !~ '[А-Яа-я]')) ";                                // не содержит кириллицу
    private static final String INSERT_INTO_REGISTER_EMAIL_PASSWORD_VALUES = "insert into register(email, password) values(?, ?)";
    private static final String SELECT_FROM_REGISTER_WHERE_EMAIL = "select * from register where email = ?";
    private static final String UPDATE_REGISTER_SET_PASSWORD_WHERE_ID = "update register set password = ? where id = ?";
    private static final String UPDATE_REGISTER_SET_EMAIL_PASSWORD_VALUES_WHERE_ID = "update register set email = ?, password = ? where id = ?";
    private static final String DELETE_FROM_REGISTER_WHERE_ID = "delete from register where id = ?";
    private static final String SELECT_FROM_REGISTER_WHERE_ID = "select * from register where id = ?";
    private static final String SELECT_COUNT_FROM_REGISTER = "select count(*) from register";


    public static void createRegisterTable() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_REGISTER_TABLE)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при попытке создания таблицы в БД", e);
        }
    }

    public static void dropRegisterTable() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DROP_REGISTER_TABLE)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при попытке удаления таблицы из БД", e);
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

    public static Register selectRegisterById(int databaseId) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_FROM_REGISTER_WHERE_ID)) {

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
            throw new RuntimeException("Ошибка при поиске пользователя с ID: " + databaseId, e);
        }
    }

    public static void postRegister(String email, String password) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_REGISTER_EMAIL_PASSWORD_VALUES)) {

            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при при попытке добавления пользователя в таблицу: " + email, e);
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
            throw new RuntimeException("Ошибка при при попытке добавлении списка пользователей в таблицу", e);
        }
    }

    public static void patchRegisterPasswordById(int databaseId, String newPassword) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTER_SET_PASSWORD_WHERE_ID)) {

            statement.setString(1, newPassword);
            statement.setInt(2, databaseId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пароля для записи таблицы по индексу: " + databaseId, e);
        }
    }

    public static void putRegisterById(int databaseId, String email, String password) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTER_SET_EMAIL_PASSWORD_VALUES_WHERE_ID)) {

            statement.setString(1, email);
            statement.setString(2, password);
            statement.setInt(3, databaseId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления записи таблицы по индексу: " + databaseId, e);

        }
    }

    public static void deleteRegisterById(int databaseId) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FROM_REGISTER_WHERE_ID)) {

            statement.setInt(1, databaseId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении записи из таблицы по ID: " + databaseId, e);
        }
    }

    public static int selectCountRegisterTable() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_FROM_REGISTER);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new RuntimeException("Не удалось получить количество записей");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении количества записей в таблице", e);
        }
    }
}
