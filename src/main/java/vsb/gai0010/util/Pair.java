package vsb.gai0010.orm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Pair<T, U> {
    private T first;
    private U second;
}
