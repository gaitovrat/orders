package vsb.gai0010.model;

import lombok.*;
import vsb.gai0010.util.Pair;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Order {
    private int id;
    private User customer;
    private Date completionDate;
    private User worker;
    private OrderStatus orderStatus;
    private List<Pair<Cloth, Integer>> clothes;
}