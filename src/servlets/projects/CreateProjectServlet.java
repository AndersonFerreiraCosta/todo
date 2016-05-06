package servlets.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Project;
import models.User;
import tools.Converters;
import daos.ProjectDao;
import daos.UserDao;

@WebServlet("/project/create")
public class CreateProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProjectDao projectDao;
	private UserDao userDao;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			projectDao = new ProjectDao();
			userDao = new UserDao();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String context = request.getContextPath();
			
			String html = "<html>";
			
			html += "<head>";
			html += "<title>New Project</title>";
			html += "</head>";
			
			html += "<body>";
			html += "<h1>Create Project</h1>";
			html += "<form action=\"" + context + "/project/create\" method=\"post\">";
			html += "  <p>Name: <input type=\"text\" name=\"name\"></p>";
			html += "  <p>Owner: " + createOwnerSelectField() + "</p>";
			html += "  <p>Members: </p>";
			html += createMembersSelectField();
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
			Project project = new Project();
			project.setName(request.getParameter("name"));
			
			//set the project owner
			int userId = Converters.stringToInt(request.getParameter("owner"));
			project.setOwner(userDao.find(userId));
			
			//set the members
			List<User> members = new ArrayList<User>();
			
			String[] membersId = request.getParameterValues("members");
			
			for (String memberId : membersId) {
				int id = Converters.stringToInt(memberId);
				members.add(userDao.find(id));
			}
			project.setMembers(members);
			
			//save the project
			projectDao.create(project);

			String context = request.getContextPath();
			
			response.sendRedirect(context + "/project/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	private String createOwnerSelectField() throws Exception {
		String html = "<select name=\"owner\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\">";
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createMembersSelectField() throws Exception {
		String html = "<select name=\"members\" multiple>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\">";
			html += user.getName();
			html += " (" + user.getRole().getName() + ")";
			html += "</option>";
		}
		
		html += "</select>";
		html += "<p><small>To select more than one member use CRTL + Click</small></p>";
		
		return html;
	}
}