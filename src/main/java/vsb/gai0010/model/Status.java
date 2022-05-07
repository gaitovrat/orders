package vsb.gai0010.orm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    NONE(1), FREE(2), BUSY(3);

    private final int id;

    public static Status of(int id) {
        Status[] values = Status.values();

        for (Status value : values) {
            if (value.getId() == id) {
                return value;
            }
        }

        return null;
    }
}
