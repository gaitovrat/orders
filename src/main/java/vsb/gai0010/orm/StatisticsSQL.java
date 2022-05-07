package vsb.gai0010.orm;

import vsb.gai0010.model.User;
import vsb.gai0010.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class StatisticsSQL {
    private static final String ORDER_STATISTICS = "SELECT SUM(c.price) \"sum\", EXTRACT(month FROM o.completion_date) \"month\" FROM \"ORDER\" o join MANY_ORDER_TO_MANY_CLOTH otc on o.id = otc.order_id join cloth c on c.id = otc.cloth_id WHERE EXTRACT(year FROM o.completion_date) = ? and completion_date IS NOT NULL GROUP BY EXTRACT(month FROM o.completion_date)";
    private static final String WORKER_STATISTICS = "SELECT COUNT(o.id) \"count\", EXTRACT(month FROM o.completion_date) \"month\" FROM \"ORDER\" o JOIN \"USER\" w ON w.id = o.worker_id and w.id = ? WHERE o.completion_date IS NOT NULL and EXTRACT(year FROM o.completion_date) = ? GROUP BY EXTRACT(month FROM o.completion_date)";

    public List<Pair<Float, Integer>> orderStatistics(int year) {
    	List<Pair<Float, Integer>> pairs = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(ORDER_STATISTICS);
        	) {
	        statement.setInt(1, year);
	        ResultSet resultSet = statement.executeQuery();
	        pairs = this.fromOrder(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return pairs;
    }

    public List<Pair<Integer, Integer>> workerStatistics(User worker, int year) {
    	List<Pair<Integer, Integer>> pairs = null;
    	
        try (
        		Database connection = Database.getConnection();
        		PreparedStatement statement = connection.create(WORKER_STATISTICS)
        	) {
	        statement.setInt(1, worker.getId());
	        statement.setInt(2, year);
	        ResultSet resultSet = statement.executeQuery();
	        pairs = this.fromWorker(resultSet);
        } catch (SQLException e) {
        	log.error(e.getMessage());
        }

        return pairs;
    }

    private List<Pair<Float, Integer>> fromOrder(ResultSet resultSet) throws SQLException {
        ArrayList<Pair<Float, Integer>> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new Pair<>(resultSet.getFloat("sum"), resultSet.getInt("month")));
        }

        return list;
    }

    private List<Pair<Integer, Integer>> fromWorker(ResultSet resultSet) throws SQLException {
        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new Pair<>(resultSet.getInt("count"), resultSet.getInt("month")));
        }

        return list;
    }
}
