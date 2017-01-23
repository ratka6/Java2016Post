package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataModel.LoginData;
import dataModel.ServerResponse;
import dataModel.User;


public class DBConnector {
	
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost/FirmaKurierska";
	
	private final String USER = "root";
	private final String PASS = "";
	
	private Connection connection;
	private Statement statement;
	
	public DBConnector(){
		System.out.println("connector in");
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ServerResponse loginCheck(LoginData loginData){
		String sql = "SELECT * FROM login_data WHERE USER = '" + loginData.id +"';";
		System.out.println(sql);
		ResultSet resultSet;
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
			System.out.println(resultSet.getString(1));
			System.out.println(resultSet.getString(2));
			if(resultSet == null){
				return new ServerResponse("error", null);
			}
			else {
				if(resultSet.getString("password").equals(loginData.password)){
					return new ServerResponse("login", User.fake());
				}
				else {
					return new ServerResponse("error", null);
				}
			}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return new ServerResponse("error", null);
	}
	

}
