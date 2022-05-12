package vsb.gai0010.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import vsb.gai0010.model.*;
import vsb.gai0010.orm.ClothSQL;
import vsb.gai0010.orm.OrderSQL;
import vsb.gai0010.orm.WorkerSQL;

import java.io.IOException;

@Log4j2
public class OrderServlet extends HttpServlet {
    private OrderSQL orderSQL;
    private ClothSQL clothSQL;
    private WorkerSQL workerSQL;

    @Override
    public void init() throws ServletException {
        super.init();

        orderSQL = new OrderSQL();
        clothSQL = new ClothSQL();
        workerSQL = new WorkerSQL();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        User user = (User) session.getAttribute(Attribute.USER.toString());
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        if (user.getRole() == Role.USER) {
            resp.sendRedirect(req.getContextPath() + "/order/customer.jsp");
        } else if (user.getRole() == Role.WORKER && user.getStatus() == Status.FREE) {
            resp.sendRedirect(req.getContextPath() + "/order/worker.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/order/info.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        OrderStatus orderStatus = OrderStatus.of(Integer.parseInt(req.getParameter("orderStatusId")));
        User user = (User) session.getAttribute(Attribute.USER.toString());

        assert orderStatus != null;

        switch (orderStatus) {
            case WAITING_FOR_PAYMENT -> {
                this.addToBasket(req, user);
                resp.sendRedirect(req.getContextPath() + "/");
            }
            case WAITING_FOR_ORDER_APPROVAL, CANCELED -> {
                Order order = orderSQL.select(Integer.parseInt(req.getParameter("orderId")));
                order.setOrderStatus(orderStatus);
                orderSQL.update(order);
                resp.sendRedirect(req.getContextPath() + "/order");
            }
            case APPROVED -> {
                Order order = orderSQL.select(Integer.parseInt(req.getParameter("orderId")));
                orderSQL.approveOrderCall(order, user);
                session.setAttribute(Attribute.USER.toString(), workerSQL.select(user.getId()));
                resp.sendRedirect(req.getContextPath() + "/order");
            }
            case SENT -> {
                Order order = orderSQL.select(Integer.parseInt(req.getParameter("orderId")));
                orderSQL.sendOrderCall(order);
                session.setAttribute(Attribute.USER.toString(), workerSQL.select(user.getId()));
                resp.sendRedirect(req.getContextPath() + "/order");
            }
        }
    }

    private void addToBasket(HttpServletRequest req, User user) {
        int count = Integer.parseInt(req.getParameter("count"));
        int clothId = Integer.parseInt(req.getParameter("clothId"));

        clothSQL.buyCloth(clothSQL.select(clothId), user, count);
    }
}
