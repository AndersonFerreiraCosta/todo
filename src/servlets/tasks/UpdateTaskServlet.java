package servlets.tasks;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Priority;
import models.Project;
import models.Status;
import models.Task;
import models.User;
import tools.Converters;
import daos.ProjectDao;
import daos.TaskDao;
import daos.UserDao;

@WebServlet("/task/update")
public class UpdateTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProjectDao projectDao;
	private TaskDao taskDao;
	private UserDao userDao;
       
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			userDao = new UserDao();
			projectDao = new ProjectDao();
			taskDao = new TaskDao(projectDao);
		} catch(Exception e ) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			int id = Converters.stringToInt(request.getParameter("id"));
			Task task = taskDao.find(id);
			
			if (task == null) {
				response.setStatus(404);
				response.getWriter().print("Task not found with id " + id);
				return;
			}
			
			String context = request.getContextPath();
			
			String html = "<html>";
			
			html += "<head>";
			html += "<title>Edit Task</title>";
			html += "</head>";
			
			html += "<body>";
			html += "<h1>Edit Task</h1>";
			html += "<form action=\"" + context + "/task/update\" method=\"post\">";
			html += "  <input type=\"hidden\" name=\"id\" value=\"" + task.getId() + "\">";
			html += "  <p>Name: <input type=\"text\" name=\"name\" value=\"" + task.getName() + "\"></p>";
			html += "  <p>Description:<br>";
			html += "    <textarea rows=\"3\" cols=\"50\" name=\"description\">";
			html += task.getDescription();
			html += "    </textarea>";
			html += "  </p>";
			html += "  <p>Priority: " + createPrioritySelectField(task) + "</p>";
			html += "  <p>Status: " + createStatusSelectField(task) + "</p>";
			html += "  <p>Project: " + createProjectSelectField(task) + "</p>";
			html += "  <p>Created by: " + createCreatedBySelectField(task) + "</p>";
			html += "  <p>Assigned to: " + createAssignedToSelectField(task) + "</p>";
			html += "  <p>Due Date: <input type=\"text\" name=\"due_date\" value=\"" 
					+ Converters.calendarToString(task.getDueDate()) + "\">(DD/MM/YYYY)</p>";
			html += "  <input type=\"submit\" value=\"Update\"></p>";
			html += "</form>";
			html += "</body>";
			
			String url = context + "/task/list";
			
			if (task.getProject() != null) {
				url += "?project_id=" + task.getProject().getId();
			}
			
			html += "<p><a href=\"" + url + "\"><< Back to list</a></p>";
	
			html += "</html>";
			
			response.setContentType("text/html");
			response.getWriter().print(html);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			int id = Converters.stringToInt(request.getParameter("id"));
			Task task = taskDao.find(id);

			int projectId = 0;
			
			if (task != null) {
				task.setName(request.getParameter("name"));
				task.setDescription(request.getParameter("description"));
				task.setPriority(Priority.valueOf(request.getParameter("priority")));
				task.setStatus(Status.valueOf(request.getParameter("status")));
				
				//set the project
				projectId = Converters.stringToInt(request.getParameter("project_id"));
				task.setProject(projectDao.find(projectId));
				
				//set the createdBy
				int createdBy = Converters.stringToInt(request.getParameter("created_by"));
				task.setCreatedBy(userDao.find(createdBy));
	
				//set the assignedTo
				int assignedTo = Converters.stringToInt(request.getParameter("assigned_to"));
				task.setAssignedTo(userDao.find(assignedTo));
				
				//due date
				task.setDueDate(Converters.stringToCalendar(request.getParameter("due_date")));
	
				//save the task
				taskDao.update(task);
			}

			String context = request.getContextPath();
			
			String url = context + "/task/list";
			
			if (projectId > 0) {
				url += "?project_id=" + projectId;
			}
			
			response.sendRedirect(url); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	private String createPrioritySelectField(Task task) throws Exception {
		String html = "<select name=\"priority\">";
		html += "<option value=\"-1\"></option>";
		
		for (Priority priority : Priority.values()) {
			html += "<option value=\"" + priority.name() + "\"";
			html += (task.getPriority() == priority 
						? " selected" : "") + ">";
			
			html += priority.name() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createStatusSelectField(Task task) throws Exception {
		String html = "<select name=\"status\">";
		html += "<option value=\"-1\"></option>";
		
		for (Status status : Status.values()) {
			html += "<option value=\"" + status.name() + "\"";
			html += (task.getStatus() == status 
						? " selected" : "") + ">";
			
			html += status.name() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createProjectSelectField(Task task) throws Exception {
		String html = "<select name=\"project_id\">";
		html += "<option value=\"-1\"></option>";
		
		for (Project project : projectDao.list()) {
			html += "<option value=\"" + project.getId() + "\"";
			html += (task.getProject().getId() == project.getId() 
						? " selected" : "") + ">";
			
			html += project.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createCreatedBySelectField(Task task) throws Exception {
		String html = "<select name=\"created_by\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\"";
			html += (task.getCreatedBy().getId() == user.getId() 
						? " selected" : "") + ">";
			
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createAssignedToSelectField(Task task) throws Exception {
		String html = "<select name=\"assigned_to\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			String selected = "";
			
			if (task.getAssignedTo() != null 
				&& task.getAssignedTo().getId() == user.getId()) {
				selected = " selected";
			}
			html += "<option value=\"" + user.getId() + "\"";
			html += selected + ">";
			
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
}
