package vsb.gai0010.db;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Role;
import vsb.gai0010.model.Status;
import vsb.gai0010.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public abstract class AbstractUserSQL implements ISQL<User> {
    public abstract String getInsertSQL();
    public abstract String getSelectSQL();
    public abstract String getSelectAllSQL();
    public abstract String getSelectIdSQL();
    public abstract String getUpdateDeleteDateSQL();
    public abstract String getUpdateIdSQL();
    public abstract String getDeleteSQL();

    public List<User> select(String login, String email, String firstName, String secondName) throws SQLException {
        login = '%' + (login == null ? "" : login) + '%';
        email = '%' + (email == null ? "" : email) + '%';
        firstName = '%' + (firstName == null ? "" : firstName) + '%';
        secondName = '%' + (secondName == null ? "" : secondName) + '%';
        List<User> users = null;
        
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(this.getSelectSQL())
        	) {
	        statement.setString(1, login);
	        statement.setString(2, email);
	        statement.setString(3, firstName);
	        statement.setString(4, secondName);
	
	        ResultSet resultSet = statement.executeQuery();
	        users = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return users;
    }

    @Override
    public List<User> select() {
    	List<User> users = null;
        
    	try (Database connection = Database.getConnection()) {
	        ResultSet resultSet = connection.execute(this.getSelectAllSQL());
	        users = this.fromResultSet(resultSet);
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}

        return users;
    }

    public User select(int id) {
    	User user = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(this.getSelectIdSQL());
        	) {
        	statement.setInt(1, id);
        	ResultSet resultSet = statement.executeQuery();
            user = fromResultSet(resultSet).get(0);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return user;
    }

    @Override
    public int insert(User user) {
        int out = -1;
        
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
	            PreparedStatement statement = connection.create(this.getInsertSQL(), new String[]{ "id" });
	
	            this.prepareUserStatement(statement, user);
	            int updateCount = statement.executeUpdate();
	
	            ResultSet generatedKeys = statement.getGeneratedKeys();
	            generatedKeys.next();
	            user.setId(generatedKeys.getInt(1));
	            
	            return updateCount;
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    public int updateDeleteDate(int id) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {        		
        		PreparedStatement preparedStatement = connection.create(this.getUpdateDeleteDateSQL());
        		preparedStatement.setInt(1, id);
        		return preparedStatement.executeUpdate();
        	});
        } catch(SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    public int update(User user) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {        		
        		PreparedStatement statement = connection.create(this.getUpdateIdSQL());
        		this.prepareUserStatement(statement, user);
        		statement.setInt(12, user.getId());
        		
        		return statement.executeUpdate();
        	});

        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }
    
    

    @Override
	public int delete(User user) {
    	int out = -1;
    	
    	try (Database connection = Database.getConnection()) {
    		out = connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create(this.getDeleteSQL());
    			statement.setInt(1, user.getId());
    			
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
    	return out;
	}
	private void prepareUserStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPhoneNumber());
        statement.setString(5, user.getFirstName());
        statement.setString(6, user.getSecondName());
        statement.setString(7, user.getCountry());
        statement.setString(8, user.getCity());
        statement.setString(9, user.getStreet());
        statement.setString(10, user.getHouseNumber());
        statement.setInt(11, user.getZip());
    }

    public List<User> fromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while(resultSet.next()) {
        	User user = User.builder()
        			.id(resultSet.getInt("id"))
        			.login(resultSet.getString("login"))
        			.password(resultSet.getString("password"))
        			.email(resultSet.getString("email"))
        			.phoneNumber(resultSet.getString("phone_number"))
        			.firstName(resultSet.getString("first_name"))
        			.secondName(resultSet.getString("second_name"))
        			.country(resultSet.getString("country"))
        			.city(resultSet.getString("city"))
        			.street(resultSet.getString("street"))
        			.houseNumber(resultSet.getString("house_number"))
        			.zip(resultSet.getInt("zip"))
        			.deletedAt(resultSet.getDate("deleted_at"))
        			.status(Status.of(resultSet.getInt("status_id")))
        			.role(Role.of(resultSet.getInt("role_id")))
        			.build();
        	users.add(user);
        }

        return users;
    }
}
