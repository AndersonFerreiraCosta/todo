package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Project;

public class ProjectDao extends BaseDao {

	private final String TABLE_NAME = "projects";

	private UsersProjectsDao usersProjectsDao;
	
	private TaskDao taskDao;
	
	private UserDao userDao;
		
	public ProjectDao() throws Exception {
		super();
		usersProjectsDao = new UsersProjectsDao();
		taskDao = new TaskDao(this);
		userDao = new UserDao();
	}

	public Project create(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "insert into " + TABLE_NAME
				+ "("
				+ " name," //1
				+ " owner" //2
				+ ") values (?, ?)";
		
		PreparedStatement pst = conn.prepareStatement(
				sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		pst.setString(1, project.getName());
		
		if (project.getOwner() == null) {
			pst.setNull(2, java.sql.Types.INTEGER);
		} else {
			pst.setInt(2, project.getOwner().getId());
		}
		
		pst.executeUpdate(); 
		
		ResultSet rs = pst.getGeneratedKeys();
		
		if (rs.next()) {
			project.setId(rs.getInt(1));
		}
		
		usersProjectsDao.create(project);
		
		close(rs);
		close(pst);
		close(conn);

		return project;
	}
	
	public Project find(int id) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id,"   //1
				+ " name," //2
				+ " owner" //3
				+ " from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, id);
		
		ResultSet rs = pst.executeQuery();

		Project project = null;
		
		if (rs.next()) {
			project = readFields(rs);
		}
		
		close(rs);
		close(pst);
		close(conn);
		
		return project;
	}

	public List<Project> list() throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id,"   //1
				+ " name," //2
				+ " owner" //3
				+ " from " + TABLE_NAME
				+ " order by name";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		List<Project> projects = new ArrayList<Project>();
		
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			Project project = readFields(rs);
			projects.add(project);
		}
		
		close(rs);
		close(pst);
		close(conn);

		return projects;
	}

	public Project update(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "update " + TABLE_NAME
				+ " set"
				+ "  name = ?,"
				+ "  owner = ?"
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setString(1, project.getName());

		if (project.getOwner() == null) {
			pst.setNull(2, java.sql.Types.INTEGER);
		} else {
			pst.setInt(2, project.getOwner().getId());
		}

		pst.setInt(3, project.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);
		
		usersProjectsDao.update(project);

		return project;
	}
	
	public void delete(Project project) throws Exception {
		//delete the user relationship
		usersProjectsDao.delete(project);
		
		//delete the project tasks
		taskDao.deleteByProject(project);
		
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setInt(1, project.getId());
		
		pst.executeUpdate();
		
		close(pst);
		close(conn);		
	}
	
	private Project readFields(ResultSet rs) throws Exception {
		Project project = new Project();
		
		project.setId(rs.getInt(1));
		project.setName(rs.getString(2));
		project.setOwner(userDao.find(rs.getInt(3)));
		
		//read users assigned to the project
		usersProjectsDao.read(project);
		
		return project;
	}
}