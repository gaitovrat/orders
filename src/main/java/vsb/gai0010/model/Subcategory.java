package vsb.gai0010.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Subcategory {
    private int id;
    private String name;
    private Category category;
}
