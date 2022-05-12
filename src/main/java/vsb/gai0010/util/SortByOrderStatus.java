package vsb.gai0010.util;

import vsb.gai0010.model.Order;

import java.util.Comparator;

public class SortByOrderStatus implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getOrderStatus().getId() - o2.getOrderStatus().getId();
    }
}
