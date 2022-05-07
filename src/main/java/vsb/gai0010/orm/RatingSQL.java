package vsb.gai0010.orm;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Cloth;
import vsb.gai0010.model.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class RatingSQL implements ISQL<Rating> {
    private static final String SELECT = "SELECT * FROM RATING";
    private static final String SELECT_ID = "SELECT * FROM RATING WHERE id = ?";
    private static final String SELECT_CLOTH_ID = "SELECT * FROM RATING WHERE cloth_id = ?";
    private static final String INSERT = "INSERT INTO RATING(rating, cloth_id) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE RATING SET rating = ?, cloth_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM RATING WHERE id = ?";

    private final ClothSQL clothSQL;

    public RatingSQL() {
        this.clothSQL = new ClothSQL();
    }

    @Override
    public List<Rating> select() {
    	List<Rating> ratings = null;
    	
        try(Database connection = Database.getConnection()) {
        	ResultSet resultSet = connection.execute(SELECT);
        	ratings = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return ratings;
    }

    @Override
    public Rating select(int id) {
    	Rating rating = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_ID)
        	) {
        	statement.setInt(1, id);
        	ResultSet resultSet = statement.executeQuery();
        	
            rating = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return rating;
    }

    public List<Rating> select(Cloth cloth) {
    	List<Rating> ratings = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_CLOTH_ID);
        	) {
	        statement.setInt(1, cloth.getId());
	        ResultSet resultSet = statement.executeQuery();
	        ratings = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return ratings;
    }

    @Override
    public int insert(Rating rating) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(INSERT, new String[]{ "id" });
        		statement.setFloat(1, rating.getRatingRaw());
        		statement.setInt(2, rating.getCloth().getId());
        		int updateCount = statement.executeUpdate();
        		
        		ResultSet generatedKeys = statement.getGeneratedKeys();
        		generatedKeys.next();
        		rating.setId(generatedKeys.getInt(1));
        		
        		return updateCount;
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public int update(Rating rating) {
    	int out = -1;
    	
        try (Database connection = Database.getConnection()) {
        	out = connection.executeTransaction(() -> {
        		PreparedStatement statement = connection.create(UPDATE);
        		
        		statement.setFloat(1, rating.getRatingRaw());
        		statement.setInt(2, rating.getCloth().getId());
        		statement.setInt(3, rating.getId());
        		
        		return statement.executeUpdate();
        	});
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return out;
    }

    @Override
	public int delete(Rating rating) {
    	int out = -1;
    	
    	try (Database connection = Database.getConnection()) {
    		out = connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create(DELETE);
    			statement.setInt(1, rating.getId());
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
		return out;
	}

	public List<Rating> fromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Rating> ratings = new ArrayList<>();

        while (resultSet.next()) {
            Cloth cloth = this.clothSQL.select(resultSet.getInt("cloth_id"));
            ratings.add(new Rating(resultSet.getInt("id"), resultSet.getFloat("rating"), cloth));
        }

        return ratings;
    }
}
