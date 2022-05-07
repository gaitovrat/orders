package vsb.gai0010.orm;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CategorySQL implements ISQL<Category> {
    private static final String SELECT = "SELECT * FROM CATEGORY";
    private static final String SELECT_ID = "SELECT * FROM CATEGORY WHERE id = ?";
    private static final String INSERT = "INSERT INTO CATEGORY(name) VALUES(?)";
    private static final String UPDATE = "UPDATE CATEGORY SET name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM CATEGORY WHERE id = ?";

    public List<Category> select() {
    	List<Category> categories = null;
    	
        try (Database connection = Database.getConnection()) {
        	ResultSet resultSet = connection.execute(SELECT);
        	categories = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return categories;
    }

    public Category select(int id) {
    	Category category = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_ID);
        	) {
        	statement.setInt(1, id);
	        ResultSet resultSet = statement.executeQuery();
            category = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return category;
    }

    @Override
    public int insert(Category category) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
            out = connection.executeTransaction(() -> {            	
            	PreparedStatement statement = connection.create(INSERT, new String[]{ "id" });
            	statement.setString(1, category.getName());
            	int updateCount = statement.executeUpdate();
            	
            	ResultSet generatedKeys = statement.getGeneratedKeys();
            	generatedKeys.next();
            	category.setId(generatedKeys.getInt(1));
            	
            	return updateCount;
            });
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public int update(Category category) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {        		
        		PreparedStatement statement = connection.create(UPDATE);
        		statement.setString(1, category.getName());
        		statement.setInt(2, category.getId());
        		
        		return statement.executeUpdate();
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }
        return out;
    }

    @Override
	public int delete(Category category) {
    	int out = -1;
    	
    	try (Database connection = Database.getConnection()) {
    		out = connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create(DELETE);
    			statement.setInt(1, category.getId());
    			
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
    	return out;
	}

	public List<Category> fromResultSet(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();

        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            categories.add(new Category(id, name));
        }

        return categories;
    }
}
