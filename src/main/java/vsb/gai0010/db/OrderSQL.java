package vsb.gai0010.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.Cloth;
import vsb.gai0010.model.Order;
import vsb.gai0010.model.OrderStatus;
import vsb.gai0010.model.User;
import vsb.gai0010.util.Pair;

@Log4j2
public class OrderSQL implements ISQL<Order> {
    private static final String SELECT = "SELECT * FROM \"ORDER\"";
    private static final String SELECT_ID = "SELECT * FROM \"ORDER\" WHERE id = ?";
    private static final String SELECT_FOR_CUSTOMER = "SELECT * FROM \"ORDER\" WHERE user_id = ?";
    private static final String SELECT_CLOTHES = "SELECT cloth_id, count FROM MANY_ORDER_TO_MANY_CLOTH WHERE order_id = ?";
    private static final String INSERT = "INSERT INTO \"ORDER\"(user_id, worker_id, order_status_id) VALUES(?, ?, ?)";
    private static final String UPDATE = "UPDATE \"ORDER\" SET worker_id = ?, order_status_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM \"ORDER\" WHERE id = ?";
    private static final String SEND_ORDER_CALL = "{ call PSendOrder(?, ?) }";
    private static final String APPROVE_ORDER_CALL = "{ call PApproveOrder(?, ?) }";

    public int approveOrderCall(Order order) {
        int out = -1;

        try (
        		Database connection = Database.getConnection();
        		CallableStatement call = connection.createCall(APPROVE_ORDER_CALL)
        	) {
            call.setInt("p_order_id", order.getId());
            call.setInt("p_worker_id", order.getWorker().getId());

            out = call.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return out;
    }

    public int sendOrderCall(Order order) throws SQLException {
    	int out = -1;

        try (        
        		Database connection = Database.getConnection();
        		CallableStatement call = connection.createCall(SEND_ORDER_CALL)
        	) {
            call.setInt("p_order_id", order.getId());
            call.setInt("p_worker_id", order.getWorker().getId());

            out = call.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public List<Order> select() {
    	List<Order> orders = null;
    	
        try (Database connection = Database.getConnection()) {
	        ResultSet resultSet = connection.execute(SELECT);
	        orders = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return orders;
    }

    public List<Order> select(User user) {
    	List<Order> orders = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_FOR_CUSTOMER);
        	) {
	        statement.setInt(1, user.getId());
	        ResultSet resultSet = statement.executeQuery();
	        orders = this.fromResultSet(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return orders;
    }

    @Override
    public Order select(int id) {
    	Order order = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(SELECT_ID)
        	) {
	        statement.setInt(1, id);
	        ResultSet resultSet = statement.executeQuery();

            order = this.fromResultSet(resultSet).get(0);
        } catch (SQLException | IndexOutOfBoundsException e) {
        	log.error(e.getMessage());
        }

        return order;
    }

    @Override
    public int insert(Order order) {
    	int out = -1;

        try (Database connection = Database.getConnection()) {
            out = connection.executeTransaction(() -> {
            	PreparedStatement statement = connection.create(INSERT, new String[]{ "id" });
            	statement.setInt(1, order.getCustomer().getId());
            	statement.setInt(2, order.getWorker().getId());
            	statement.setInt(3, order.getOrderStatus().getId());
            	
            	int countUpdate = statement.executeUpdate();
            	
            	ResultSet generatedKeys = statement.getGeneratedKeys();
            	generatedKeys.next();
            	order.setId(generatedKeys.getInt(1));
            	return countUpdate;
            });

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return out;
    }

    @Override
    public int update(Order order) {
    	int out = -1;

        try (Database connection = Database.getConnection()) {
            out = connection.executeTransaction(() -> {            	
            	PreparedStatement statement = connection.create(UPDATE);
            	statement.setInt(1, order.getWorker().getId());
            	statement.setInt(2, order.getOrderStatus().getId());
            	statement.setInt(3, order.getId());
            	
            	return statement.executeUpdate();
            });
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return out;
    }

    @Override
	public int delete(Order order) {
    	int out = -1;
    	
		try (
				Database connection = Database.getConnection();
				PreparedStatement statement = connection.create(DELETE)
			) {
			statement.setInt(1, order.getId());
			out = statement.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return out;
	}

	public List<Order> fromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        CustomerSQL customerSQL = new CustomerSQL();
        WorkerSQL workerSQL = new WorkerSQL();

        while (resultSet.next()) {
        	int id = resultSet.getInt("id");
        	Order order = Order.builder()
        			.id(id)
        			.customer(customerSQL.select(resultSet.getInt("user_id")))
        			.worker(workerSQL.select(resultSet.getInt("worker_id")))
        			.completionDate(resultSet.getDate("completion_date"))
        			.orderStatus(OrderStatus.of(resultSet.getInt("order_status_id")))
        			.clothes(this.getClothes(id))
        			.build();
            orders.add(order);
        }

        return orders;
    }
    
    private List<Pair<Cloth, Integer>> getClothes(int orderId) {
    	ArrayList<Pair<Cloth,Integer>> clothes = new ArrayList<>();
    	ClothSQL clothSQL = new ClothSQL();
    	
    	try (
    			Database connection = Database.getConnection();
    			PreparedStatement statement = connection.create(SELECT_CLOTHES);
    		) {
    		statement.setInt(1, orderId);
    		ResultSet resultSet = statement.executeQuery();
    		
    		while (resultSet.next()) {
    			Cloth cloth = clothSQL.select(resultSet.getInt("cloth_id"));
    			clothes.add(new Pair<>(cloth, resultSet.getInt("count")));
    		}
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
    	return clothes;
    }
}
