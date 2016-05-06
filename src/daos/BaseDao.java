package daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import factories.DbConnectionFactory;

public abstract class BaseDao {
	
	protected DbConnectionFactory factory;
	
	public BaseDao() throws Exception {
		factory = DbConnectionFactory.getInstance();
	}
	
	protected void close(Connection conn) {
		try {
			conn.close();
		} catch(Exception e) {}
	}

	protected void close(Statement pst) {
		try {
			pst.close();
		} catch(Exception e) {}
	}

	protected void close(ResultSet rs) {
		try {
			rs.close();
		} catch(Exception e) {}
	}
}