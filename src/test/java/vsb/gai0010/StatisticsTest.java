package vsb.gai0010;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import vsb.gai0010.db.StatisticsSQL;
import vsb.gai0010.db.WorkerSQL;
import vsb.gai0010.model.User;
import vsb.gai0010.util.Pair;

public class StatisticsTest {
    private static StatisticsSQL statisticsSQL;
    private static WorkerSQL workerSQL;
    
    @BeforeClass
    public static void init() {
    	statisticsSQL = new StatisticsSQL();
    	workerSQL = new WorkerSQL();
    }

    // Funkce 6.1
    @Test
    public void statisticsOrder() throws SQLException {
        List<Pair<Float, Integer>> pairs = statisticsSQL.orderStatistics(2022);
        Assert.assertNotNull(pairs);
    }

    @Test
    public void statisticsWorker() throws SQLException {
        User worker = workerSQL.select().get(0);
        List<Pair<Integer, Integer>> pairs = statisticsSQL.workerStatistics(worker, 2022);
        Assert.assertNotNull(pairs);
    }
}
