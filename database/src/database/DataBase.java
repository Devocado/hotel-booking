package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import persistence.DataSource;
import types.Customer;
import types.Reservation;
import types.Room;

public class DataBase implements DataSource {
	
	private static final String DATABASE_PROPERTIES =
			"/home/jesse/Jesse/Java/EclipseProjects/HotelBooking/database/resources/database.properties";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/hotel";
	
	//customers table
	private static final String CUSTOMER_TABLE = "customers";
	private static final String FIRST_NAME = "f_name";
	private static final String LAST_NAME = "l_name";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String PHONE = "phone";
	
	@SuppressWarnings("unused")
	private static final String RESERVATION_TABLE = "reservations";
	
	private static final String ROOM_TABLE = "rooms";
	private static final String ROOM_ID = "id";
	private static final String ROOM_PRICE = "price_per_night";
	private static final String MAX_GUESTS = "max_guests";
	
	private Connection conn;
	
	public DataBase() {
		Properties p = new Properties();
		try (BufferedReader buff = new BufferedReader(new FileReader(DATABASE_PROPERTIES))) {
			
			p.load(buff);
			conn = DriverManager.getConnection(DATABASE_URL, p);
		} 
		catch (IOException e) {
			throw new RuntimeException("Missing database properties");
			//e.printStackTrace();
		} catch (SQLException sqle) {
			throw new RuntimeException("Could not connect to database");
		}
	}

	@Override
	public boolean addCustomer(Customer customer, String password) {
	    final String INSERT_CUST = "INSERT INTO "+CUSTOMER_TABLE+" ("+FIRST_NAME+", "+LAST_NAME+", "+EMAIL+
	            ", "+PASSWORD+", "+PHONE+")"+ " VALUES(?, ?, ?, ?, ?)";
	    
	    boolean insertSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(INSERT_CUST)) {
	        pStmt.setString(1, customer.getFirstName());
	        pStmt.setString(2, customer.getLastName());
	        pStmt.setString(3, customer.getEmail());
	        pStmt.setString(4, password);
	        pStmt.setString(5, customer.getPhone());
	        
	        insertSucceeded = pStmt.executeUpdate() == 1;
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
		return insertSucceeded;
	}

	@Override
	public Customer fetchCustomer(String email) {
	    final String GET_CUST = "SELECT "+FIRST_NAME+", "+LAST_NAME+", "+PHONE+" FROM "+CUSTOMER_TABLE+" WHERE "+EMAIL+"= ?";
	    
	    Customer cust = null;
	    
		try (PreparedStatement pStmt = conn.prepareStatement(GET_CUST)) {
		    pStmt.setString(1, email);
		    ResultSet rs = pStmt.executeQuery();
		    if (rs.first() ) {
		        cust = new Customer(rs.getString(1), rs.getString(2), email, rs.getString(3));
		    }
		    
		} catch (SQLException sqle) {
		    System.err.println(sqle.getMessage());
		}
		return cust;
	}
	
	@Override
	public boolean updateCustomer(Customer oldCust, Customer newCust) { 
	    
	    final String UPDATE_CUST = "UPDATE "+CUSTOMER_TABLE+" SET "+FIRST_NAME+"= ?, "+
	            LAST_NAME+"= ?, "+EMAIL+"= ?, "+PHONE+"= ?  WHERE "+EMAIL+"= ?";
	    
	    boolean updateSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(UPDATE_CUST)) {
	        
	        pStmt.setString(1, newCust.getFirstName());
	        pStmt.setString(2, newCust.getLastName());
	        pStmt.setString(3, newCust.getEmail());
	        pStmt.setString(4, newCust.getPhone());
	        pStmt.setString(5, oldCust.getEmail());
	        
	        updateSucceeded = pStmt.executeUpdate() == 1;
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
	    
	    return updateSucceeded;
	}
	
	@Override
	public boolean updateCustomerPassword(Customer cust, String newPassword) {
	    final String UPDATE_CUST = "UPDATE "+CUSTOMER_TABLE+" SET "+PASSWORD+"= ?  WHERE "+EMAIL+"= ?";
        
        boolean updateSucceeded = false;
        
        try (PreparedStatement pStmt = conn.prepareStatement(UPDATE_CUST)) {
            
            pStmt.setString(1, newPassword);
            pStmt.setString(2, cust.getEmail());
            
            updateSucceeded = pStmt.executeUpdate() == 1;
            
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
        
        return updateSucceeded;
	}
	
	@Override
	public boolean deleteCustomer(Customer customer) {
	    return deleteCustomer(customer.getEmail());
	}
	
	public boolean deleteCustomer(String email) {
	    final String DELETE_CUST = "DELETE FROM "+CUSTOMER_TABLE+" WHERE "+EMAIL+"= ?";
	    
	    boolean deleteSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(DELETE_CUST)) {
	        pStmt.setString(1, email);
	        
	        int rowsAffected = pStmt.executeUpdate();
	        assert rowsAffected <= 1: "Panic Stations!! More than one customer was deleted!!";
	        deleteSucceeded = rowsAffected == 1;
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
	    return deleteSucceeded;
	}
	
	@Override
	public String fetchPassword(String email) {
	    final String GET_PASS = "SELECT "+PASSWORD+" FROM "+CUSTOMER_TABLE+" WHERE "+EMAIL+"= ?";
	    
	    String password = "";
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(GET_PASS) ) {
	        
	        pStmt.setString(1, email);
	        ResultSet rs = pStmt.executeQuery();
	        
	        if(rs.first()) {
	            password = rs.getString(PASSWORD);
	        }
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	        
	    }
	    return password;
	}

	@Override
	public boolean saveReservation(Reservation reservation) {
		throw new UnsupportedOperationException("Not implemented"); 
	}

	@Override
	public Reservation fetchReservation(int id) {
	    throw new UnsupportedOperationException("Not implemented"); 
	}
	
	@Override
    public List<Reservation> fetchReservations(Customer customer) {
	    throw new UnsupportedOperationException("Not implemented");
	}

    @Override
    public boolean deleteReservation(Reservation reservation) {
        throw new UnsupportedOperationException("Not implemented"); 
    }

    @Override
    public boolean updateReservation(Reservation oldReservation, Reservation newReservation) {
        throw new UnsupportedOperationException("Not implemented"); 
    }
	
    @Override
    public Room getRoom(int roomNumber) {
        final String GET_ROOM = "SELECT "+ROOM_PRICE+", "+MAX_GUESTS+" FROM "+ROOM_TABLE+" WHERE "+ROOM_ID+"= ?";
        
        Room room = null;
        
        try (PreparedStatement pStmt = conn.prepareStatement(GET_ROOM)) {
            pStmt.setInt(1, roomNumber);
            ResultSet rs = pStmt.executeQuery();
            
            if (rs.first()) {
                room = new Room(roomNumber, rs.getBigDecimal(ROOM_PRICE), rs.getInt(MAX_GUESTS));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return room;
    }
    
    @Override
    public List<Room> getRooms() {
        
        final String GET_ROOMS = "SELECT * FROM "+ROOM_TABLE;
        
        List<Room> rooms = new ArrayList<>();
        
        try (PreparedStatement pStmt = conn.prepareStatement(GET_ROOMS)){
            ResultSet rs = pStmt.executeQuery();
            
            while(rs.next()) {
                rooms.add(new Room(rs.getInt(ROOM_ID), rs.getBigDecimal(ROOM_PRICE), rs.getInt(MAX_GUESTS)));
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
        return rooms;
    }
    
    @Override
    public List<Room> getUnreservedRooms() {
        return null;
    }
    
}
