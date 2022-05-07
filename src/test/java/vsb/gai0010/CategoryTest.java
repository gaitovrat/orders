package vsb.gai0010;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;
import vsb.gai0010.db.CategorySQL;
import vsb.gai0010.model.Category;

import java.sql.SQLException;
import java.util.List;

@Log4j2
public class CategoryTest {
    private static Category testCategory;
    private static CategorySQL categorySQL;
    
    @BeforeClass
    public static void init() {
    	testCategory = new Category(0, "test");
    	categorySQL = new CategorySQL();
    	
    	log.debug("Insert category");
    	
    	categorySQL.insert(testCategory);
    }
    
    @AfterClass
    public static void deinit() {
    	log.debug("Delete category");
    	categorySQL.delete(testCategory);
    }

    @Test
    public void create() throws SQLException {
    	Category category = new Category(0, "test 1");
        int i = categorySQL.insert(category);
        Assert.assertEquals(1, i);
        categorySQL.delete(category);
    }

    @Test
    public void getAll() throws SQLException {
        List<Category> select = categorySQL.select();
        Assert.assertTrue(select.size() > 0);
    }
}
