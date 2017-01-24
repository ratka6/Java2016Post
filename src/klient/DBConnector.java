package klient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import dataModel.KurierPackageInfo;
import dataModel.LoginData;
import dataModel.Package;
import dataModel.PackageStatus;
import dataModel.ServerResponse;
import dataModel.Statuses;
import dataModel.User;


public class DBConnector {
	
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost/FirmaKurierska";
	
	private final String USER = "root";
	private final String PASS = "";
	
	private Connection connection;
	private Statement statement;
	private User user;
	
	public DBConnector(){
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
	
	public ServerResponse packagesInfo(User user){
		try {
			ArrayList<PackageStatus> arrayList = new ArrayList<>();
			System.out.println(user);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM package JOIN user on package.userId = user.id where user.name = '" + user.getName()+"'");
			while(resultSet.next()){
				arrayList.add(new PackageStatus(String.valueOf(resultSet.getString(1)), resultSet.getString("status"), new Package(resultSet.getDouble("height")
						, resultSet.getDouble("width"), resultSet.getDouble("length"), resultSet.getDouble("weight"), resultSet.getDouble("cost"),
						resultSet.getString("sourceAddress"),resultSet.getString("destinationAddress"),resultSet.getString("date"))));
			}
			if(arrayList.isEmpty()){
				return new ServerResponse("error", null);
			}
			else{
				return new ServerResponse("packagesInfo", arrayList);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ServerResponse("error", null);
	}
	
	public ServerResponse orderCourier(Package package1){
		try {
			ResultSet resultSet = statement.executeQuery("select kurier.id, COUNT(*) from kurier join package on kurier.id = package.kurierId");
			int kurier = 0;
			while(resultSet.next()){
				if(resultSet.getInt(2) < 25){
					kurier = resultSet.getInt(1);
					break;
				}
			}
			if(kurier == 0){
				return new ServerResponse("error", null);
			}
			else{
				System.out.println(user);
				statement.executeUpdate("INSERT INTO `package` (`id`, `height`, `width`, `length`, `weight`, `cost`, `sourceAddress`, `destinationAddress`, `date`, `status`, `userId`, `kurierId`)"
						+ " VALUES (NULL, '"+package1.getHeight()+"', '"+package1.getWidth()+"', '"+package1.getLength()+"', '"+package1.getWeight()+"', '"+package1.getCost()+"',"
								+ " '"+package1.getSourceAddress()+"', '"+package1.getDestinationAddres()+"', '"+package1.getDate()+"', 'UNKNOWN', '"+userGetId()+"', '"+kurier+"');");
				return new ServerResponse("orderCourier", null);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ServerResponse("error", null);
	}
	


	public ServerResponse kurierLogin(LoginData log){
		String sql = "SELECT * FROM kurier_login WHERE USER = '" + log.id +"';";
		ResultSet resultSet;
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				if(resultSet.getString("password").equals(log.password)){
					resultSet = statement.executeQuery("SELECT kurier.name, kurier.address, kurier.phoneNumber, kurier.email from kurier where kurier.email ='" +log.id+"'");
					if(resultSet.next()){
						user = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
					}
					return new ServerResponse("kurierLogin", user);
				}
				else {
					return new ServerResponse("error", null);
				}
			}
			else {
				return new ServerResponse("error", null);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return new ServerResponse("error", null);
	}
	
	public ServerResponse kurierRegister(User u){
		String sql = "SELECT * FROM kurier_login where user = '" + u.getEmail() +"';";
		try {
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				return new ServerResponse("error", null);
			}
			else{
				statement.executeUpdate("INSERT INTO kurier (`name`, `address`, `phoneNumber`, `email`) VALUES ('"+u.getName()+"', '"+u.getAddress()+"', '"+u.getPhoneNumber()+"', '"+u.getEmail()+"');");
				String pass = generatePassword();
				statement.executeUpdate("INSERT INTO kurier_login (`user`, `password`) VALUES ('"+u.getEmail()+"', '"+pass+"');");
				return new ServerResponse("kurierRegister", new LoginData(u.getEmail(), pass));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ServerResponse("error", null);
	}
	
	public ServerResponse kurierPackagesInfo(User u){
		try {
			ResultSet resultSet = statement.executeQuery("SELECT kurier.id FROM kurier where kurier.name = '" + u.getName() +"'");
			int kurierId = 0;
			if(resultSet.next()){
				kurierId = resultSet.getInt(1);
			}
			resultSet = statement.executeQuery("select package.*,user.name,user.address,user.phoneNumber,user.email from kurier "
									+ "join package on kurier.id = package.kurierId join user on package.userId = user.id where kurier.id = '"+kurierId+"'");
			ArrayList<KurierPackageInfo> arrayList = new ArrayList<>();
			while(resultSet.next()){
				arrayList.add(new KurierPackageInfo(new PackageStatus(String.valueOf(resultSet.getInt("id")), resultSet.getString("status"),new Package(resultSet.getDouble("height")
						, resultSet.getDouble("width"), resultSet.getDouble("length"), resultSet.getDouble("weight"), resultSet.getDouble("cost"),
						resultSet.getString("sourceAddress"),resultSet.getString("destinationAddress"),resultSet.getString("date"))), new User(resultSet.getString("name"),resultSet.getString("address"), resultSet.getString("phoneNumber"), resultSet.getString("email"))));
			}
			return new ServerResponse("kurierPackagesInfo", arrayList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerResponse changeStatus(String[] change){
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM package WHERE package.id = '"+Integer.parseInt(change[0])+"'");
			if(resultSet.next()){
				if(resultSet.getString("status").equals("Statuses."+change[1])){
					return new ServerResponse("error", null);
				}
				else{
					statement.executeUpdate("UPDATE `package` SET `status` = '"+change[1]+"' WHERE `package`.`id` = "+Integer.parseInt(change[0])+";");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerResponse login(LoginData loginData){
		String sql = "SELECT * FROM login_data WHERE USER = '" + loginData.id +"';";
		System.out.println(sql);
		ResultSet resultSet;
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				if(resultSet.getString("password").equals(loginData.password)){
					resultSet = statement.executeQuery("SELECT user.name, user.address, user.phoneNumber, user.email from user where user.email = '" +loginData.id+"'");
					if(resultSet.next()){
						User u = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
						this.user = u;
						System.out.println(u);
					}
					System.out.println(user);
					return new ServerResponse("login", user);
				}
				else {
					return new ServerResponse("error", null);
				}
			}
			else {
				return new ServerResponse("error", null);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return new ServerResponse("error", null);
	}

	public ServerResponse register(User u) {
		String sql = "SELECT * FROM login_data where user = '" + u.getEmail() +"';";
		try {
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				return new ServerResponse("error", null);
			}
			else{
				statement.executeUpdate("INSERT INTO `user` (`name`, `address`, `phoneNumber`, `email`) VALUES ('"+u.getName()+"', '"+u.getAddress()+"', '"+u.getPhoneNumber()+"', '"+u.getEmail()+"');");
				String pass = generatePassword();
				statement.executeUpdate("INSERT INTO `login_data` (`user`, `password`) VALUES ('"+u.getEmail()+"', '"+pass+"');");
				return new ServerResponse("register", new LoginData(u.getEmail(), pass));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ServerResponse("error", null);
	}

	private String generatePassword() {
		Random r = new Random();
		int pass = r.nextInt(100000);
		return String.valueOf(pass);
	}
	
	private Integer userGetId() {
		try {
			ResultSet resultSet = statement.executeQuery("Select * from user where email = '"+user.getEmail()+"'");
			if(resultSet.next()){
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
