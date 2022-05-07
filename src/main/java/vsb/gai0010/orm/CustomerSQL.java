package vsb.gai0010.orm.db;

public class CustomerSQL extends AbstractUserSQL {
    private static final String INSERT = "INSERT INTO \"USER\"(login, password, email, phone_number, first_name, second_name, country, city, street, house_number, zip, status_id, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 2)";
    private static final String SELECT = "SELECT * FROM \"USER\" WHERE login LIKE ? AND email LIKE ? AND first_name LIKE ? AND second_name LIKE ? AND deleted_at is null AND role_id = 2 AND status_id = 1";
    private static final String SELECT_ALL = "SELECT * FROM \"USER\" WHERE deleted_at is null AND deleted_at is null AND role_id = 2 AND status_id = 1";
    private static final String SELECT_ID = "SELECT * FROM \"USER\" WHERE id = ? AND role_id = 2 AND status_id = 1";
    private static final String UPDATE_DELETE_DATE =  "UPDATE \"USER\" SET deleted_at = sysdate WHERE id = ? AND deleted_at is null AND role_id = 2 AND status_id = 1";
    private static final String UPDATE_ID = "UPDATE \"USER\" SET login = ?, password = ?, email = ?, phone_number = ?, first_name = ?, second_name = ?, country = ?, city = ?, street = ?, house_number = ?, zip = ? WHERE id = ? AND deleted_at is null AND role_id = 2 AND status_id = 1";
    private static final String DELETE = "DELETE FROM \"USER\" WHERE id = ? AND deleted_at is null AND role_id = 2 AND status_id = 1";
    

    @Override
    public String getInsertSQL() {
        return INSERT;
    }

    @Override
    public String getSelectSQL() {
        return SELECT;
    }

    @Override
    public String getSelectAllSQL() {
        return SELECT_ALL;
    }

    @Override
    public String getSelectIdSQL() {
        return SELECT_ID;
    }

    @Override
    public String getUpdateDeleteDateSQL() {
        return UPDATE_DELETE_DATE;
    }

    @Override
    public String getUpdateIdSQL() {
        return UPDATE_ID;
    }

	@Override
	public String getDeleteSQL() {
		return DELETE;
	}
}
