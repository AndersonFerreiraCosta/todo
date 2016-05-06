package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Project;
import models.User;

public class UsersProjectsDao extends BaseDao {
	
	private final String TABLE_NAME = "users_projects";
	
	private UserDao userDao;

	public UsersProjectsDao() throws Exception {
		super();
		userDao = new UserDao();
	}
	
	public void create(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "insert into " + TABLE_NAME
				+ " (user_id, project_id)"
				+ " values(?, ?);";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		for (User user : project.getMembers()) {
			pst.setInt(1, user.getId());
			pst.setInt(2, project.getId());
			pst.executeUpdate();
		}
		
		close(pst);
		close(conn);
	}
	
	public void read(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select user_id"
				+ " from " + TABLE_NAME
				+ " where project_id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, project.getId());
		
		ResultSet rs = pst.executeQuery();
		
		List<User> users = new ArrayList<User>();
		
		while (rs.next()) {
			users.add(userDao.find(rs.getInt(1)));
		}
		
		project.setMembers(users);
		
		close(pst);
		close(rs);
		close(conn);
	}
	
	public void delete(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where project_id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, project.getId());
		pst.executeUpdate();
		
		close(pst);
	}
	
	public void update(Project project) throws Exception {
		delete(project);
		create(project);
	}
}
