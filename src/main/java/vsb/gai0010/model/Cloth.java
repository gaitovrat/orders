package vsb.gai0010.orm.model;

import java.util.List;

import lombok.*;
import vsb.gai0010.orm.util.Pair;

@Data
@AllArgsConstructor
@Builder
public class Cloth {
    private int id;
    private String name;
    private float price;
    private int count;
    private Subcategory subcategory;
    private String description;
    private List<Pair<Order, Integer>> orders;
}
