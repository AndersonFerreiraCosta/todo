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

@WebServlet("/task/create")
public class CreateTaskServlet extends HttpServlet {
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
			
			String context = request.getContextPath();
			
			String html = "<html>";
			
			html += "<head>";
			html += "<title>New Task</title>";
			html += "</head>";
			
			html += "<body>";
			html += "<h1>New Task</h1>";
			html += "<form action=\"" + context + "/task/create\" method=\"post\">";
			html += "  <input type=\"hidden\" name=\"id\">";
			html += "  <p>Name: <input type=\"text\" name=\"name\"></p>";
			html += "  <p>Description:<br>";
			html += "    <textarea rows=\"3\" cols=\"50\" name=\"description\">";
			html += "    </textarea>";
			html += "  </p>";
			html += "  <p>Priority: " + createPrioritySelectField() + "</p>";
			html += "  <p>Status: " + createStatusSelectField() + "</p>";
			html += "  <p>Project: " + createProjectSelectField() + "</p>";
			html += "  <p>Created by: " + createCreatedBySelectField() + "</p>";
			html += "  <p>Assigned to: " + createAssignedToSelectField() + "</p>";
			html += "  <p>Due Date: <input type=\"text\" name=\"due_date\">(DD/MM/YYYY)</p>";
			html += "  <input type=\"submit\" value=\"Create\"></p>";
			html += "</form>";
			html += "</body>";
			
			html += "<p><a href=\"" + context + "/project/list\"><< Back to list</a></p>";
	
			html += "</html>";
			
			response.setContentType("text/html");
			response.getWriter().print(html);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Task task = new Task();
			task.setName(request.getParameter("name"));
			task.setDescription(request.getParameter("description"));
			task.setPriority(Priority.valueOf(request.getParameter("priority")));
			task.setStatus(Status.valueOf(request.getParameter("status")));
			
			//set the project
			int projectId = Converters.stringToInt(request.getParameter("project_id"));
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
			taskDao.create(task);

			String context = request.getContextPath();
			
			response.sendRedirect(context + "/task/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	private String createPrioritySelectField() throws Exception {
		String html = "<select name=\"priority\">";
		html += "<option value=\"-1\"></option>";
		
		for (Priority priority : Priority.values()) {
			html += "<option value=\"" + priority.name() + "\">";
			html += priority + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createStatusSelectField() throws Exception {
		String html = "<select name=\"status\">";
		html += "<option value=\"-1\"></option>";
		
		for (Status status : Status.values()) {
			html += "<option value=\"" + status.name() + "\">";
			html += status + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createProjectSelectField() throws Exception {
		String html = "<select name=\"project_id\">";
		html += "<option value=\"-1\"></option>";
		
		for (Project project : projectDao.list()) {
			html += "<option value=\"" + project.getId() + "\">";
			html += project.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createCreatedBySelectField() throws Exception {
		String html = "<select name=\"created_by\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\">";
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createAssignedToSelectField() throws Exception {
		String html = "<select name=\"assigned_to\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\">";
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
}
