package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Priority;
import models.Project;
import models.Status;
import models.Task;
import tools.Converters;

public class TaskDao extends BaseDao {

	private final String TABLE_NAME = "tasks";
	
	private ProjectDao projectDao;
	private UserDao userDao;
	
	public TaskDao(ProjectDao projectDao) throws Exception {
		super();
		
		this.projectDao = projectDao;
		userDao = new UserDao();
	}

	public Task create(Task task) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "insert into " + TABLE_NAME
				+ "("
				+ " name,"        //1
				+ " description," //2
				+ " priority,"    //3
				+ " status,"      //4
				+ " due_date,"    //5
				+ " project_id,"  //6
				+ " created_by,"  //7
				+ " assigned_to"  //8
				+ ") values (?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pst = conn.prepareStatement(
				sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		pst.setString(1, task.getName());
		pst.setString(2, task.getDescription());
		pst.setString(3, task.getPriority().name());
		pst.setString(4, task.getStatus().name());
		pst.setTimestamp(5, Converters.calendarToTimestamp(task.getDueDate()));
		
		//set the project 
		if (task.getProject() == null) {
			pst.setNull(6, java.sql.Types.INTEGER);
		} else {
			pst.setInt(6, task.getProject().getId());
		}
		
		//set the user who created the task
		if (task.getCreatedBy() == null) {
			pst.setNull(7, java.sql.Types.INTEGER);
		} else {
			pst.setInt(7, task.getCreatedBy().getId());
		}
		
		//set the user who the task is assigned to
		if (task.getAssignedTo() == null) {
			pst.setNull(8, java.sql.Types.INTEGER);
		} else {
			pst.setInt(8, task.getAssignedTo().getId());
		}

		pst.executeUpdate(); 
		
		ResultSet rs = pst.getGeneratedKeys();
		
		if (rs.next()) {
			task.setId(rs.getInt(1));
		}
		
		close(rs);
		close(pst);
		close(conn);

		return task;
	}
	
	public Task find(int id) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id,"          //1
				+ " name,"        //2
				+ " description," //3
				+ " priority,"    //4
				+ " status,"      //5
				+ " due_date,"    //6
				+ " project_id,"  //7
				+ " created_by,"  //8
				+ " assigned_to"  //9
				+ " from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, id);
		
		ResultSet rs = pst.executeQuery();

		Task task = null;
		
		if (rs.next()) {
			task = readFields(rs);
		}
		
		close(rs);
		close(pst);
		close(conn);
		
		return task;	
	}

	public List<Task> list() throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id,"          //1
				+ " name,"        //2
				+ " description," //3
				+ " priority,"    //4
				+ " status,"      //5
				+ " due_date,"    //6
				+ " project_id,"  //7
				+ " created_by,"  //8
				+ " assigned_to " //9
				+ " from " + TABLE_NAME
				+ " order by id";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		List<Task> tasks = new ArrayList<Task>();
		
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			Task task = readFields(rs);
			tasks.add(task);
		}
		
		close(rs);
		close(pst);
		close(conn);

		return tasks;
	}
	
	public List<Task> listByProject(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "select "
				+ " id,"          //1
				+ " name,"        //2
				+ " description," //3
				+ " priority,"    //4
				+ " status,"      //5
				+ " due_date,"    //6
				+ " project_id,"  //7
				+ " created_by,"  //8
				+ " assigned_to " //9
				+ " from " + TABLE_NAME
				+ " where project_id = ?"
				+ " order by id";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, project.getId());
		
		List<Task> tasks = new ArrayList<Task>();
		
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			Task task = readFields(rs);
			tasks.add(task);
		}
		
		close(rs);
		close(pst);
		close(conn);

		return tasks;
	}


	public Task update(Task task) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "update " + TABLE_NAME
				+ " set"
				+ "  name = ?,"        //1
				+ "  description = ?," //2
				+ "  priority = ?,"    //3
				+ "  status = ?,"      //4
				+ "  due_date = ?,"    //5
				+ "  project_id = ?,"  //6
				+ "  created_by = ?,"  //7
				+ "  assigned_to = ?"  //8
				+ " where id = ?";     //9
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setString(1, task.getName());
		pst.setString(2, task.getDescription());
		pst.setString(3, task.getPriority().name());
		pst.setString(4, task.getStatus().name());
		pst.setTimestamp(5, Converters.calendarToTimestamp(task.getDueDate()));
		
		if (task.getProject() == null) {
			pst.setNull(6, java.sql.Types.INTEGER);
		} else {
			pst.setInt(6, task.getProject().getId());
		}
		
		//set the user who created the task
		if (task.getCreatedBy() == null) {
			pst.setNull(7, java.sql.Types.INTEGER);
		} else {
			pst.setInt(7, task.getCreatedBy().getId());
		}
		
		//set the user who the task is assigned to
		if (task.getAssignedTo() == null) {
			pst.setNull(8, java.sql.Types.INTEGER);
		} else {
			pst.setInt(8, task.getAssignedTo().getId());
		}

		pst.setInt(9, task.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);

		return task;
	}
	
	public void delete(Task task) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setInt(1, task.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);
	}
	
	private Task readFields(ResultSet rs) throws Exception {
		Task task = new Task();
		
		task.setId(rs.getInt(1));
		task.setName(rs.getString(2));
		task.setDescription(rs.getString(3));
		task.setPriority(Priority.valueOf(rs.getString(4)));
		task.setStatus(Status.valueOf(rs.getString(5)));
		task.setDueDate(Converters.timestampToCalendar(rs.getTimestamp(6)));
		task.setProject(projectDao.find(rs.getInt(7)));
		task.setCreatedBy(userDao.find(rs.getInt(8)));
		task.setAssignedTo(userDao.find(rs.getInt(9)));
		
		return task;
	}

	public void deleteByProject(Project project) throws Exception {
		Connection conn = factory.getConnection();
		
		String sql = "delete from " + TABLE_NAME
				+ " where project_id = ?";
		
		PreparedStatement pst = conn.prepareStatement(sql);
		
		pst.setInt(1, project.getId());
		
		pst.executeUpdate(); 
		
		close(pst);
		close(conn);
	}

}