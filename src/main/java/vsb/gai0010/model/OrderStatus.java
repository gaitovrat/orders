package vsb.gai0010.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    WAITING_FOR_PAYMENT(1),
    WAITING_FOR_ORDER_APPROVAL(2),
    APPROVED(3),
    SENT(4),
    CANCELED(5);

    private final int id;

    public static OrderStatus of(int id) {
        OrderStatus[] values = OrderStatus.values();
        for (OrderStatus value : values) {
            if (value.id == id) {
                return value;
            }
        }

        return null;
    }
}
