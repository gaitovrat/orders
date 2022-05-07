package vsb.gai0010.orm;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Cloth;
import vsb.gai0010.model.Order;
import vsb.gai0010.model.Subcategory;
import vsb.gai0010.model.User;
import vsb.gai0010.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ClothSQL implements ISQL<Cloth> {
    private static final String INSERT = "INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM CLOTH";
    private static final String SELECT_ID = "SELECT * FROM CLOTH WHERE id = ?";
    private static final String SELECT_FILTER = "SELECT * FROM CLOTH WHERE name LIKE ? AND subcategory_id = ? AND price >= ?";
	private static final String UPDATE = "UPDATE CLOTH SET name = ?, price = ?, count = ?, subcategory_id = ?, description = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM CLOTH WHERE id = ?";
    private static final String CALL = "{call PBuyCloth(?, ?, ?)}";

    private final SubcategorySQL subcategorySQL;

    public ClothSQL() {
        this.subcategorySQL = new SubcategorySQL();
    }

    @Override
    public List<Cloth> select() {
    	List<Cloth> cloths = null;
    	
        try (Database connection = Database.getConnection()) {
	        ResultSet resultSet = connection.execute(SELECT);
	        cloths = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return cloths;
    }

    @Override
    public Cloth select(int id) {
    	Cloth cloth = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_ID)
        	) {
	        statement.setInt(1, id);
	        ResultSet resultSet = statement.executeQuery();
            cloth = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return cloth;
    }

    public List<Cloth> select(String name, Subcategory subcategory, float minPrice) {
        List<Cloth> cloths = null;
        name = '%' + (name == null ? "" : name) + '%';

        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_FILTER)
        	) {
	        statement.setString(1, name);
	        statement.setInt(2, subcategory.getId());
	        statement.setFloat(3, minPrice);
	        ResultSet resultSet = statement.executeQuery();
	
	        cloths = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return cloths;
    }

    @Override
    public int insert(Cloth cloth) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(INSERT, new String[]{ "id" });
        		prepareStatement(statement, cloth);
        		int countUpdate = statement.executeUpdate();
        		
        		ResultSet generatedKeys = statement.getGeneratedKeys();
        		generatedKeys.next();
        		cloth.setId(generatedKeys.getInt(1));
        		
        		return countUpdate;
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public int update(Cloth cloth) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(UPDATE);
        		this.prepareStatement(statement, cloth);
        		statement.setInt(6, cloth.getId());
        		return statement.executeUpdate();
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    public int buyCloth(Cloth cloth, User user, int count) {
    	int out = -1;
    	
        try (
        		Database connection = Database.getConnection();
        		CallableStatement call = connection.createCall(CALL)
        	) {
            call.setInt("p_cloth_id", cloth.getId());
            call.setInt("p_user_id", user.getId());
            call.setInt("p_count", count);

            out = call.executeUpdate();
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }
    
    @Override
	public int delete(Cloth cloth) {
    	int out = -1;
    	
    	try (Database connection = Database.getConnection()) {
    		out = connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create(DELETE);
    			statement.setInt(1, cloth.getId());
    			
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
		return out;
	}

	public List<Cloth> fromResultSet(ResultSet resultSet) throws SQLException {
        List<Cloth> cloths = new ArrayList<>();

        while(resultSet.next()) {
            Subcategory subcategory = this.subcategorySQL.select(resultSet.getInt("subcategory_id"));
            int id = resultSet.getInt("id");
            
            Cloth cloth = Cloth.builder()
            		.id(id)
            		.name(resultSet.getString("name"))
            		.price(resultSet.getFloat("price"))
            		.count(resultSet.getInt("count"))
            		.subcategory(subcategory)
            		.description(resultSet.getString("description"))
            		.build();
            
            cloths.add(cloth);
        }

        return cloths;
    }
    
    private PreparedStatement prepareStatement(PreparedStatement statement, Cloth cloth) throws SQLException {
        statement.setString(1, cloth.getName());
        statement.setFloat(2, cloth.getPrice());
        statement.setInt(3, cloth.getCount());
        statement.setInt(4, cloth.getSubcategory().getId());
        statement.setString(5, cloth.getDescription());

        return statement;
    }

}
