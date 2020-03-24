package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;

import java.util.Properties;

import types.Customer;
import types.DataSource;
import types.Reservation;

public class DataBase implements DataSource {
	
	private static final String DATABASE_PROPERTIES =
			"/home/jesse/Jesse/Java/EclipseProjects/HotelBooking/database/resources/database.properties";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/hotel";
	
//	public static void main(String[] args) {
//		Properties p = new Properties();
//		try {
//			MysqlDataSource dataSource = new MysqlDataSource();
//			dataSource.setUser("hotelapp");
//	        dataSource.setPassword("qazwsx");
//	        dataSource.setServerName("localhost");
//	        dataSource.setDatabaseName("hotel");
//	        
//	        Connection conn  = dataSource.getConnection();
//	        
//	        System.out.println("Connection is working: " + conn.isValid(10));
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public DataBase() {
		Properties p = new Properties();
		try (BufferedReader buff = new BufferedReader(new FileReader(DATABASE_PROPERTIES))) {
			
			p.load(buff);
			
			Connection conn = DriverManager.getConnection(DATABASE_URL, p);

	        Statement stmnt = conn.createStatement();
	        
	        ResultSet result = stmnt.executeQuery("SELECT * FROM customers");
	        
	        System.out.println(result);
	        
	        while(result.next()) {
	        	System.out.printf("%s %s %s %n", result.getString(2), result.getString(3), result.getString(4) );
	        }
	        
	        
		} 
		catch (IOException e) {
			//throw new RuntimeException("Missing database properties");
			e.printStackTrace();
		} catch (SQLException sqle) {
			throw new RuntimeException("Could not connect to database");
		}
	}

	@Override
	public boolean saveCustomer(Customer customer) {
		System.out.println("Saving customer");
		return false;
	}

	@Override
	public Customer fetchCustomer(String email) {
		System.out.println("Fetching customer");
		return null;
	}

	@Override
	public boolean saveReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reservation fetchReservation(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
