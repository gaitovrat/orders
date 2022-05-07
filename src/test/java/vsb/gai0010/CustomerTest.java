package vsb.gai0010;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.db.CustomerSQL;
import vsb.gai0010.model.Role;
import vsb.gai0010.model.Status;
import vsb.gai0010.model.User;

import java.sql.SQLException;
import java.util.List;


@Log4j2
public class CustomerTest {
	private static User testUser;
    private static CustomerSQL customerSQL;
    
    @BeforeClass
    public static void init() {
    	testUser = User.builder()
    			.status(Status.NONE)
    			.role(Role.USER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	log.debug("Insert user");
    	customerSQL = new CustomerSQL();
    	customerSQL.insert(testUser);
    }
    
    @AfterClass
    public static void deinit() {
    	customerSQL.delete(testUser);
    }

    // Funkce 1.1
    @Test
    public void create() throws SQLException {
    	User user = User.builder()
    			.status(Status.NONE)
    			.role(Role.USER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
        int out = customerSQL.insert(user);

        Assert.assertEquals(1, out);
        
        customerSQL.delete(user);
    }

    // Funkce 1.2
    @Test
    public void search() throws SQLException {
        List<User> users = customerSQL.select(testUser.getLogin(), testUser.getEmail(), testUser.getFirstName(), testUser.getSecondName());
        Assert.assertNotNull(users);
    }

    // Funkce 1.3
    @Test
    public void get() throws SQLException {
        User userFromDB = customerSQL.select(testUser.getId());
        Assert.assertEquals(testUser, userFromDB);
    }

    // Funkce 1.4
    @Test
    public void delete() {
    	User user = User.builder()
    			.status(Status.NONE)
    			.role(Role.USER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	
    	customerSQL.insert(user);
        int i = customerSQL.updateDeleteDate(user.getId());
        Assert.assertNotEquals(i, -1);
        Assert.assertNotEquals(customerSQL.select(user.getId()).getDeletedAt(), null);
        
        customerSQL.delete(user);
    }

    // Funkce 1.5
    @Test
    public void update() throws SQLException {
        String newLogin = "tes";

        testUser.setLogin(newLogin);
        customerSQL.update(testUser);
        User user = customerSQL.select(testUser.getId());

        Assert.assertEquals(newLogin, user.getLogin());
        
        testUser.setLogin("test");
        customerSQL.update(testUser);
    }
}
