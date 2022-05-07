package vsb.gai0010.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    FAILURE(1),
    IO(2),
    PARSER(3),
    USER_NOT_FOUND(4);

    private final int number;
}
