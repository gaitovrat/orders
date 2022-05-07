package vsb.gai0010.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vsb.gai0010.model.Cloth;
import vsb.gai0010.model.Order;
import vsb.gai0010.model.OrderStatus;
import vsb.gai0010.model.User;
import vsb.gai0010.orm.ClothSQL;
import vsb.gai0010.orm.OrderSQL;

import java.io.IOException;
import java.util.List;

public class UpdateOrderServlet extends HttpServlet {
    private ClothSQL clothSQL;
    private OrderSQL orderSQL;

    @Override
    public void init() throws ServletException {
        super.init();

        this.clothSQL = new ClothSQL();
        this.orderSQL = new OrderSQL();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        OrderStatus status = OrderStatus.of(Integer.parseInt(req.getParameter("status")));
        User user = (User) session.getAttribute(Attribute.USER.toString());
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/signin.jsp");
            return;
        }


        List<Order> orders = this.orderSQL.select(user).stream().filter(el -> el.getOrderStatus() == OrderStatus.WAITING_FOR_PAYMENT).toList();
        // TODO: Get count from request
        switch (status) {
            case WAITING_FOR_PAYMENT -> {
                Cloth cloth = (Cloth) session.getAttribute(Attribute.CLOTH.toString());
                this.clothSQL.buyCloth(cloth, user, 1);
            }
            case WAITING_FOR_ORDER_APPROVAL -> {
                for (Order order : orders) {
                    order.setOrderStatus(OrderStatus.WAITING_FOR_ORDER_APPROVAL);
                    this.orderSQL.update(order);
                }
            }
        }

        resp.sendRedirect(req.getHeader("referer"));
    }
}
