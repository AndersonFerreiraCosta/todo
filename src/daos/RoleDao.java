package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Role;

public class RoleDao extends BaseDao {
	
	private final String TABLE_NAME = "roles";

	public RoleDao() throws Exception {
		super();
	}

	public Role create(Role role) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "insert into " + TABLE_NAME
				+ "("
				+ "name, manager"
				+ ") values (?, ?)";
		
		PreparedStatement pst = conn.prepareStatement(
				sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		pst.setString(1, role.getName());
		pst.setBoolean(2, role.isManager());
		pst.executeUpdate(); 
		
		ResultSet rs = pst.getGeneratedKeys();
		
		if (rs.next()) {
			role.setId(rs.getInt(1));
		}
		
		close(rs);
		close(pst);
		close(conn);

		return role;
	}
	
	public Role find(int id) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id, name, manager "
				+ " from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, id);
		
		ResultSet rs = pst.executeQuery();

		Role role = null;
		
		if (rs.next()) {
			role = readFields(rs);
		}
		
		close(rs);
		close(pst);
		close(conn);
		
		return role;	
	}
	
	public List<Role> list() throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id, name, manager "
				+ " from " + TABLE_NAME
				+ " order by name";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		List<Role> roles = new ArrayList<Role>();
		
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			Role role = readFields(rs);
			roles.add(role);
		}
		
		close(rs);
		close(pst);
		close(conn);

		return roles;
	}
	
	public Role update(Role role) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "update " + TABLE_NAME
				+ " set"
				+ "  name = ?,"
				+ "  manager = ?"
				+ "  where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setString(1, role.getName());
		pst.setBoolean(2, role.isManager());
		pst.setInt(3, role.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);

		return role;
	}
	
	public void delete(Role role) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setInt(1, role.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);
	}
	
	private Role readFields(ResultSet rs) throws Exception {
		Role role = new Role();
		
		role.setId(rs.getInt(1));
		role.setName(rs.getString(2));
		role.setManager(rs.getBoolean(3));
		
		return role;
	}
}