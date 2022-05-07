package vsb.gai0010;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.db.WorkerSQL;
import vsb.gai0010.model.Role;
import vsb.gai0010.model.Status;
import vsb.gai0010.model.User;

import java.sql.SQLException;
import java.util.List;

@Log4j2
public class WorkerTest {
    private static User testWorker;
    private static WorkerSQL workerSQL;

    @BeforeClass
    public static void init() {
    	testWorker = User.builder()
    			.status(Status.FREE)
    			.role(Role.WORKER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	log.info("Insert worker");
    	workerSQL = new WorkerSQL();
    	workerSQL.insert(testWorker);
    }
    
    @AfterClass
    public static void deinit() {
    	workerSQL.delete(testWorker);
    }
    
    // Funkce 3.1
    @Test
    public void create() throws SQLException {
    	User worker = User.builder()
    			.status(Status.FREE)
    			.role(Role.WORKER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
        int out = workerSQL.insert(worker);

        Assert.assertEquals(1, out);
        
        workerSQL.delete(worker);
    }

    // Funkce 3.2
    @Test
    public void search() throws SQLException {
        List<User> users = workerSQL.select(testWorker.getLogin(), testWorker.getEmail(), testWorker.getFirstName(), testWorker.getSecondName());
        Assert.assertNotNull(users);
    }

    // Funkce 3.3
    @Test
    public void get() throws SQLException {
        User userFromDB = workerSQL.select(testWorker.getId());
        Assert.assertEquals(testWorker, userFromDB);
    }

    // Funkce 3.4
    @Test
    public void delete() throws SQLException {
    	User worker = User.builder()
    			.status(Status.FREE)
    			.role(Role.WORKER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	workerSQL.insert(worker);
        int i = workerSQL.updateDeleteDate(worker.getId());
        Assert.assertEquals(1, i);
        Assert.assertNotEquals(workerSQL.select(worker.getId()).getDeletedAt(), null);
    }
}
