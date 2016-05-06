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

@WebServlet("/project/update")
public class UpdateProjectServlet extends HttpServlet {
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
			
			int id = Converters.stringToInt(request.getParameter("id"));
			Project project = projectDao.find(id);
			
			if (project == null) {
				response.setStatus(404);
				response.getWriter().print("Project not found with id " + id);
				return;
			}
			
			String context = request.getContextPath();
			
			String html = "<html>";
			
			html += "<head>";
			html += "<title>Edit Project</title>";
			html += "</head>";
			
			html += "<body>";
			html += "<h1>Edit Project</h1>";
			html += "<form action=\"" + context + "/project/update\" method=\"post\">";
			html += "  <input type=\"hidden\" name=\"id\" value=\"" + project.getId() + "\">";
			html += "  <p>Name: <input type=\"text\" name=\"name\" value=\"" + project.getName() + "\"></p>";
			html += "  <p>Owner: " + createOwnerSelectField(project) + "</p>";
			html += "  <p>Members: </p>";
			html += createMembersSelectField(project);
			html += "  <input type=\"submit\" value=\"Update\"></p>";
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
			int id = Converters.stringToInt(request.getParameter("id"));
			Project project = projectDao.find(id);
			
			if (project != null) {

				project.setName(request.getParameter("name"));
				
				//set the project owner
				int userId = Converters.stringToInt(request.getParameter("owner"));
				project.setOwner(userDao.find(userId));
				
				//set the members
				List<User> members = new ArrayList<User>();
				
				String[] membersId = request.getParameterValues("members");
				
				for (String memberId : membersId) {
					int mId = Converters.stringToInt(memberId);
					members.add(userDao.find(mId));
				}
				project.setMembers(members);
				
				//save the project
				projectDao.update(project);
			}
			
			String context = request.getContextPath();
			
			response.sendRedirect(context + "/project/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	private String createOwnerSelectField(Project project) throws Exception {
		String html = "<select name=\"owner\">";
		html += "<option value=\"-1\"></option>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\"";
			html += (project.getOwner().getId() == user.getId() 
						? " selected" : "") + ">";
			
			html += user.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createMembersSelectField(Project project) throws Exception {
		String html = "<select name=\"members\" multiple>";
		
		for (User user : userDao.list()) {
			html += "<option value=\"" + user.getId() + "\"";
			html += (project.getMembers().contains(user) 
					? " selected" : "") + ">";
			html += user.getName();
			html += " (" + user.getRole().getName() + ")";
			html += "</option>";
		}
		
		html += "</select>";
		html += "<p><small>To select more than one member use CRTL + Click</small></p>";
		
		return html;
	}
}