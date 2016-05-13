package factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnectionFactory {
	
	private static DbConnectionFactory instance;
	
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/todo";
	private String username = "root";
	private String password = "";
	
	private DbConnectionFactory() throws Exception {
		//carrega driver do banco de dados
		Class.forName(driver); 
	}
	
	public static DbConnectionFactory getInstance() throws Exception {
		//se instance for null precisamos criar uma nova instancia
		if (instance == null) {
			instance = new DbConnectionFactory();
		}
		
		return instance;
	}
	
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection(url, username, password);
	}
}