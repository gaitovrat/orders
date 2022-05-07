package vsb.gai0010.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(1), USER(2), WORKER(3);

    private final int id;

    public static Role of(int id) {
        Role[] values = Role.values();
        for (Role value : values) {
            if (value.id == id) {
                return value;
            }
        }

        return null;
    }
}
