package vsb.gai0010.db;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Category;
import vsb.gai0010.model.Subcategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SubcategorySQL implements ISQL<Subcategory> {
    private static final String SELECT = "SELECT * FROM SUBCATEGORY";
    private static final String SELECT_ID = "SELECT * FROM SUBCATEGORY WHERE id = ?";
    private static final String INSERT = "INSERT INTO SUBCATEGORY(name, category_id) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE SUBCATEGORY SET name = ?, category_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM SUBCATEGORY WHERE id = ?";
    
    private final CategorySQL categorySQL;

    public SubcategorySQL() {
        categorySQL = new CategorySQL();
    }

    @Override
    public Subcategory select(int id) {
        Subcategory subcategory = null;
        
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_ID)
        	) {
	        statement.setInt(1, id);
	        ResultSet resultSet = statement.executeQuery();
            subcategory = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return subcategory;
    }

    @Override
    public int insert(Subcategory subcategory) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(INSERT, new String[]{ "id" });
        		statement.setString(1, subcategory.getName());
        		statement.setInt(2, subcategory.getCategory().getId());
        		
        		int updateCount = statement.executeUpdate();
        		
        		ResultSet generatedKeys = statement.getGeneratedKeys();
        		generatedKeys.next();
        		subcategory.setId(generatedKeys.getInt(1));
        		
        		return updateCount;
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public int update(Subcategory subcategory) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(UPDATE);
        		
        		statement.setString(1, subcategory.getName());
        		statement.setInt(2, subcategory.getCategory().getId());
        		statement.setInt(3, subcategory.getId());
        		
        		return statement.executeUpdate();
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public List<Subcategory> select() {
    	List<Subcategory> subcategories = null;
    	
        try (Database connection = Database.getConnection()) {
	        ResultSet resultSet = connection.execute(SELECT);
	        subcategories = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return subcategories;
    }

    @Override
	public int delete(Subcategory subcategory) {
    	int out = -1;
    	
    	try (Database connection = Database.getConnection()) {
    		out = connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create(DELETE);
    			statement.setInt(1, subcategory.getId());
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
		return out;
	}

	public List<Subcategory> fromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        while (resultSet.next()) {
            int categoryId = resultSet.getInt("category_id");
            Category category = this.categorySQL.select(categoryId);
            subcategories.add(new Subcategory(resultSet.getInt("id"), resultSet.getString("name"), category));
        }

        return subcategories;
    }
}
