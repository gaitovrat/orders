package vsb.gai0010.model;

import java.util.List;

import lombok.*;
import vsb.gai0010.util.Pair;

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
}
