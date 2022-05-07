package vsb.gai0010.util;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowingSupplier<T> {
	T get() throws SQLException;
}
