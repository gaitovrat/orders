package vsb.gai0010.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vsb.gai0010.orm.ClothSQL;
import vsb.gai0010.orm.OrderSQL;

import java.io.IOException;

public class OrderItemServlet extends HttpServlet {
    private OrderSQL orderSQL;
    private ClothSQL clothSQL;

    @Override
    public void init() throws ServletException {
        super.init();

        this.orderSQL = new OrderSQL();
        this.clothSQL = new ClothSQL();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int clothId = Integer.parseInt(req.getParameter("clothId"));

        orderSQL.deleteItem(this.orderSQL.select(orderId), this.clothSQL.select(clothId));

        resp.sendRedirect(req.getContextPath() + "/order");
    }
}
