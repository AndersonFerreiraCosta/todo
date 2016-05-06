package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.User;

public class UserDao extends BaseDao {
	
	private final String TABLE_NAME = "users";
	
	private RoleDao roleDao;
	
	public UserDao() throws Exception {
		super();
		
		roleDao = new RoleDao();
	}

	public User create(User user) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "insert into " + TABLE_NAME
				+ "("
				+ "name, email, salt, hash, role_id"
				+ ") values (?, ?, ?, ?, ?)";
		
		PreparedStatement pst = conn.prepareStatement(
				sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		//encrypt user password
		user.encryptPassword();
		
		pst.setString(1, user.getName());
		pst.setString(2, user.getEmail());
		pst.setString(3, user.getSalt());
		pst.setString(4, user.getHash());
		
		if (user.getRole() == null) {
			pst.setNull(5, java.sql.Types.INTEGER);
		} else {
			pst.setInt(5, user.getRole().getId());
		}
		
		pst.executeUpdate(); 
		
		ResultSet rs = pst.getGeneratedKeys();
		
		if (rs.next()) {
			user.setId(rs.getInt(1));
		}
		
		close(rs);
		close(pst);
		close(conn);

		return user;
	}
	
	public User findByEmail(String email) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id, name, email, salt, hash, role_id"
				+ " from " + TABLE_NAME
				+ " where email = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, email);
		
		ResultSet rs = ps.executeQuery();
		
		User user = null;
		
		if (rs.next()) {
			user = readFields(rs);
		}
		
		close(rs);
		close(ps);
		close(conn);
		
		return user;
	}
	
	public User find(int id) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id, name, email, salt, hash, role_id "
				+ " from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, id);
		
		ResultSet rs = pst.executeQuery();

		User user = null;
		
		if (rs.next()) {
			user = readFields(rs);
		}
		
		close(rs);
		close(pst);
		close(conn);
		
		return user;
	}
	
	public List<User> list() throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id, name, email, salt, hash, role_id "
				+ " from " + TABLE_NAME
				+ " order by name";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		List<User> users = new ArrayList<User>();
		
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			User user = readFields(rs);
			users.add(user);
		}
		
		close(rs);
		close(pst);
		close(conn);

		return users;
	}

	public User update(User user) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "update " + TABLE_NAME
				+ " set"
				+ "  name = ?,"
				+ "  email = ?,"
				+ "  salt = ?,"
				+ "  hash = ?,"
				+ "  role_id = ?"
				+ "  where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		//encrypt user password
		user.encryptPassword();

		pst.setString(1, user.getName());
		pst.setString(2, user.getEmail());
		pst.setString(3, user.getSalt());
		pst.setString(4, user.getHash());
		
		if (user.getRole() == null) {
			pst.setNull(5, java.sql.Types.INTEGER);
		} else {
			pst.setInt(5, user.getRole().getId());
		}
		
		pst.setInt(6, user.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);

		return user;
	}
	
	public void delete(User user) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setInt(1, user.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);
	}
	
	private User readFields(ResultSet rs) throws Exception {
		User user = new User();
		
		user.setId(rs.getInt(1));
		user.setName(rs.getString(2));
		user.setEmail(rs.getString(3));
		user.setSalt(rs.getString(4));
		user.setHash(rs.getString(5));
		user.setRole(roleDao.find(rs.getInt(6)));
		
		return user;
	}
}