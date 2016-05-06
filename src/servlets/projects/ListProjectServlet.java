package servlets.projects;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Project;
import daos.ProjectDao;

@WebServlet("/project/list")
public class ListProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = request.getContextPath();
		
		String html = "<html>";
		html += "<head>";
		html += "<title>List of Projects</title>";
		html += "</head>";
		
		html += "<body>";
		html += "<h1>List of Projects</h1>";
		html += "<p><a href=\"" + context + "/project/create\">Add New</a></p>";
		html += "<table>";
		html += "<tr>";
		html += "<th>Id</th>";
		html += "<th>Name</th>";
		html += "<th>Owner</th>";
		html += "<th>Number of Members</th>";
		html += "</tr>";
		
		try {
			ProjectDao projectDao = new ProjectDao();
			for (Project project : projectDao.list()) {
				html += "<tr>";
				html += "<td>" + project.getId() + "</td>";
				html += "<td>" + project.getName() + "</td>";
				html += "<td>" + project.getOwner().getName() + "</td>";
				html += "<td align=\"center\">" + project.getMembers().size() + "</td>";
				html += "<td>" + createEditLink(context, project) + "</td>";
				html += "<td>" + createDeleteLink(context, project) + "</td>";
				html += "</tr>";
			}
		} catch(Exception e) {
			throw new ServletException(e);
		}
		
		html += "</table>";
		
		html += "<p><a href=\"" + context + "/index.jsp\"><< Back to menu</a></p>";

		html += "</body>";
		html += "</html>";
		
		response.setContentType("text/html");
		response.getWriter().print(html);
	}
	
	private String createEditLink(String context, Project project) {
		String html = "<a href=\"" + context 
				+ "/project/update?id=" + project.getId()
				+ "\">Edit</a>";
		
		return html;
	}
	
	private String createDeleteLink(String context, Project project) {
		String html = "<a href=\"" + context
				+ "/project/delete?id=" + project.getId()
				+ "\">Delete</a>";
		
		return html;
	}
}