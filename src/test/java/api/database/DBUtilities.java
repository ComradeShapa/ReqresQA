package api.database;

import api.DTO.Register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBUtilities {

    public static void createRegisterTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String createTableSql = "create table if not exists register(" +
                    "id serial primary key, " +
                    "email varchar(100) not null, " +
                    "password varchar(100) not null)";

            statement.executeUpdate(createTableSql);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void dropRegisterTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String dropTableSql = "drop table if exists register";
            statement.executeUpdate(dropTableSql);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void addNewUser(String email, String password, Connection connection) {
        try {
            String addRegisterSql = "insert into register(email, password) values(?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(addRegisterSql);
            prepStatement.setString(1, email);
            prepStatement.setString(2, password);
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void addListOfAccounts(List<Register> generatedAccountsList, Connection connection) {
        try {
            String addRegisterSql = "insert into register(email, password) values(?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(addRegisterSql);

            for(Register account : generatedAccountsList) {
                prepStatement.setString(1, account.getEmail());
                prepStatement.setString(2, account.getPassword());
                prepStatement.addBatch();
            }
            prepStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
