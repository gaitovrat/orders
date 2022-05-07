package vsb.gai0010.orm;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class UserSQL extends AbstractUserSQL {
    private static final String INSERT = "INSERT INTO \"USER\"(login, password, email, phone_number, first_name, second_name, country, city, street, house_number, zip, status_id, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 2)";
    private static final String SELECT = "SELECT * FROM \"USER\" WHERE login LIKE ? AND email LIKE ? AND first_name LIKE ? AND second_name LIKE ? AND deleted_at is null";
    private static final String SELECT_LOGIN_PASSWORD = "SELECT * FROM \"USER\" WHERE login LIKE ? AND password LIKE ? AND deleted_at is null";
    private static final String SELECT_ALL = "SELECT * FROM \"USER\" WHERE deleted_at is null AND deleted_at is null";
    private static final String SELECT_ID = "SELECT * FROM \"USER\" WHERE id = ?";
    private static final String UPDATE_DELETE_DATE =  "UPDATE \"USER\" SET deleted_at = sysdate WHERE id = ? AND deleted_at is null";
    private static final String UPDATE_ID = "UPDATE \"USER\" SET login = ?, password = ?, email = ?, phone_number = ?, first_name = ?, second_name = ?, country = ?, city = ?, street = ?, house_number = ?, zip = ? WHERE id = ? AND deleted_at is null";
    private static final String DELETE = "DELETE FROM \"USER\" WHERE id = ? AND deleted_at is null";

    public User select(String login, String password) {
        User user = null;
        try (
                Database connection = Database.getConnection();
                PreparedStatement statement = connection.create(SELECT_LOGIN_PASSWORD)
        ) {
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            user = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        }

        return user;
    }

    @Override
    public String getInsertSQL() {
        return INSERT;
    }

    @Override
    public String getSelectSQL() {
        return SELECT;
    }

    @Override
    public String getSelectAllSQL() {
        return SELECT_ALL;
    }

    @Override
    public String getSelectIdSQL() {
        return SELECT_ID;
    }

    @Override
    public String getUpdateDeleteDateSQL() {
        return UPDATE_DELETE_DATE;
    }

    @Override
    public String getUpdateIdSQL() {
        return UPDATE_ID;
    }

    @Override
    public String getDeleteSQL() {
        return DELETE;
    }
}
