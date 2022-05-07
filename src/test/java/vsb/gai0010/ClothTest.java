package vsb.gai0010;

import lombok.extern.log4j.Log4j2;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import vsb.gai0010.model.*;
import vsb.gai0010.orm.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ClothTest {
	private static User testUser;
    private static Category testCategory;
    private static Subcategory testSubcategory;
    private static Cloth testCloth;

    private static ClothSQL clothSQL = new ClothSQL();
    private static CategorySQL categorySQL;
    private static SubcategorySQL subcategorySQL;
    private static RatingSQL ratingSQL;
    private static CustomerSQL customerSQL = new CustomerSQL();

    @BeforeClass
    public static void init() {
    	ratingSQL = new RatingSQL();
    	
    	testCategory = new Category(0, "test");    	
    	log.info("Insert category");
    	categorySQL = new CategorySQL();
    	categorySQL.insert(testCategory);
    	
    	testSubcategory = new Subcategory(0, "test", testCategory);
    	log.info("Insert subcategory");
    	subcategorySQL = new SubcategorySQL();
    	subcategorySQL.insert(testSubcategory);
    	
    	testCloth = Cloth.builder()
        		.name("test")
        		.price(0.0f)
        		.count(10)
        		.subcategory(testSubcategory)
        		.description("description")
        		.build();
    	log.info("Insert cloth");
    	clothSQL = new ClothSQL();
    	clothSQL.insert(testCloth);
    	testCloth = clothSQL.select(testCloth.getId());
    	
    	testUser = User.builder()
    			.status(Status.NONE)
    			.role(Role.USER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	log.info("Insert user");
    	customerSQL = new CustomerSQL();
    	customerSQL.insert(testUser);
    }
    
    @AfterClass
    public static void deinit() {
    	try (Database connection = Database.getConnection()) {
    		connection.executeTransaction(() -> {
    			PreparedStatement statement = connection.create("DELETE FROM MANY_ORDER_TO_MANY_CLOTH WHERE cloth_id =?");
    			statement.setInt(1, testCloth.getId());
    			return statement.executeUpdate();
    		});
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    	}
    	
    	clothSQL.delete(testCloth);
    	subcategorySQL.delete(testSubcategory);
    	categorySQL.delete(testCategory);
    	
		OrderSQL orderSQL = new OrderSQL();
		List<Order> orders = orderSQL.select(testUser);
		for (Order order : orders) {
			orderSQL.delete(order);
		}
    	
    	customerSQL.delete(testUser);
    }

    // Funkce 2.1
    @Test
    public void create() {
    	Cloth cloth = Cloth.builder()
        		.name("test")
        		.price(1.23f)
        		.count(10)
        		.subcategory(testSubcategory)
        		.description("description")
        		.build();
        int out = clothSQL.insert(cloth);
        Assert.assertEquals(1, out);
        clothSQL.delete(cloth);
    }

    // Funkce 2.2
    @Test
    public void search() {
        List<Cloth> test_cloth = clothSQL.select("test", testSubcategory, 0.0f);
        Assert.assertNotNull(test_cloth);
    }

    // Funkce 2.3
    @Test
    public void get() {
    	Cloth cloth = Cloth.builder()
        		.name("test")
        		.price(1.23f)
        		.count(10)
        		.subcategory(testSubcategory)
        		.description("description")
        		.build();
    	clothSQL.insert(cloth);
        Cloth select = clothSQL.select(cloth.getId());
        Assert.assertEquals(select, cloth);
        clothSQL.delete(cloth);
    }

    // Funkce 2.4
    @Test
    public void update() {
        String name = "test2";

        testCloth.setName(name);
        int out = clothSQL.update(testCloth);
        Cloth cloth = clothSQL.select(testCloth.getId());

        Assert.assertEquals(1, out);
        Assert.assertEquals(cloth.getName(), name);
        
        testCloth.setName("test");
        clothSQL.update(testCloth);
    }

    // Funkce 2.5
    @Test
    public void rate() {
        Rating rating = new Rating(0, 3.4f, testCloth);
		int out = ratingSQL.insert(rating);
        Assert.assertEquals(1, out);
        ratingSQL.delete(rating);
    }

    // Funkce 2.6
    @Test
    public void buy() throws SQLException {
        int count = 10;
        int out = clothSQL.buyCloth(testCloth, testUser, count);

        Assert.assertNotEquals(out, -1);
    }
}
