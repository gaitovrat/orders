package vsb.gai0010.db;

import java.util.List;

public interface ISQL<T> {
    List<T> select();
    T select(int id);
    int insert(T t);
    int update(T t);
    int delete(T t);
}
