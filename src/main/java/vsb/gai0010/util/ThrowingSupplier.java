package vsb.gai0010.orm.util;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowingSupplier<T> {
	T get() throws SQLException;
}
