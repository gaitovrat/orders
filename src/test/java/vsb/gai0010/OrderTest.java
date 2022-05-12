package vsb.gai0010;

import lombok.extern.log4j.Log4j2;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import vsb.gai0010.orm.CustomerSQL;
import vsb.gai0010.orm.OrderSQL;
import vsb.gai0010.orm.WorkerSQL;
import vsb.gai0010.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class OrderTest {
    private static OrderSQL orderSQL;
    private static CustomerSQL customerSQL;
    private static WorkerSQL workerSQL;

    private static User customerTest;
    private static User workerTest;
    private static Order orderTest;

    @BeforeClass
    public static void init() throws SQLException {
    	customerTest = User.builder()
    			.status(Status.NONE)
    			.role(Role.USER)
    			.login("test cs")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	log.info("Insert customer");
    	customerSQL = new CustomerSQL();
    	customerSQL.insert(customerTest);
    	
    	workerTest = User.builder()
    			.status(Status.FREE)
    			.role(Role.WORKER)
    			.login("test")
    			.password("test")
    			.phoneNumber("test")
    			.build();
    	log.info("Insert worker");
    	workerSQL = new WorkerSQL();
        workerSQL.insert(workerTest);

        orderTest = Order.builder().customer(customerTest).worker(workerTest).orderStatus(OrderStatus.WAITING_FOR_PAYMENT).build();
        log.info("Insert Order");
        orderSQL = new OrderSQL();
        orderSQL.insert(orderTest);
    }
    
    @AfterClass
    public static void deinit() {
    	orderSQL.delete(orderTest);
    	workerSQL.delete(workerTest);
    	customerSQL.delete(customerTest);
    }

    // Funkce 4.1
    @Test
    public void create() {
    	Order order = Order.builder().customer(customerTest).worker(workerTest).orderStatus(OrderStatus.WAITING_FOR_PAYMENT).build();
        int out = orderSQL.insert(order);

        Assert.assertEquals(1, out);
        
        orderSQL.delete(order);
    }

    // Funkce 4.3
    @Test
    public void search() {
        List<Order> orders = orderSQL.select(customerTest);
        Assert.assertNotNull(orders);
    }

    // Funkce 4.4
    @Test
    public void get() throws SQLException {
        Order order1 = orderSQL.select(orderTest.getId());
        orderTest.setClothes(new ArrayList<>());

        Assert.assertEquals(order1, orderTest);
    }

    // Funkce 4.5
    @Test
    public void cancel() throws SQLException {
        orderTest.setOrderStatus(OrderStatus.CANCELED);
        int update = orderSQL.update(orderTest);

        Assert.assertEquals(1, update);
        
        orderTest.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);;
        orderSQL.update(orderTest);
    }

    // Funkce 4.6
    @Test
    public void paid() throws SQLException {
        orderTest.setOrderStatus(OrderStatus.WAITING_FOR_ORDER_APPROVAL);
        int update = orderSQL.update(orderTest);

        Assert.assertEquals(1, update);
        
        orderTest.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);;
        orderSQL.update(orderTest);
    }

    // Funkce 4.7
    @Test
    public void sendOrder() throws SQLException {
        int out = orderSQL.sendOrderCall(orderTest);
        Assert.assertEquals(1, out);
    }

    // Funkce 4.8
    @Test
    public void approveOrder() throws SQLException {
        int out = orderSQL.approveOrderCall(orderTest, workerTest);
        Assert.assertEquals(1, out);
    }
}
