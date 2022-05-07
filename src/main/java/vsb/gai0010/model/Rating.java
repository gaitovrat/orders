package vsb.gai0010.orm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rating {
    private int id;
    private float ratingRaw;
    private Cloth cloth;
}
