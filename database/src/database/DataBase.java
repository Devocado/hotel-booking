package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import persistence.DataSource;
import types.Customer;
import types.Reservation;
import types.Room;

@SuppressWarnings("unused")
public class DataBase implements DataSource {
	
	private static final String DATABASE_PROPERTIES =
			"/home/jesse/Jesse/Java/LongTermProjects/Hotel/database/resources/database.properties";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/hotel";
	
	
	//customers table
	private static final String CUSTOMER_TABLE = "customers";
	private static final String CUST_ID = "customers.id";
	private static final String FIRST_NAME = "f_name";
	private static final String LAST_NAME = "l_name";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String PHONE = "phone";
	
	//reservations table
	private static final String RESERVATION_TABLE = "reservations";
	private static final String RES_ID = "reservations.id";
	private static final String CUST_ID_FK = "reservations.customer_id";
	private static final String START = "reservations.start_date";
	private static final String END = "reservations.end_date";
	
	//rooms table
	private static final String ROOM_TABLE = "rooms";
	private static final String ROOM_ID = "rooms.id";
	private static final String ROOM_PRICE = "rooms.price_per_night";
	private static final String MAX_GUESTS = "rooms.max_guests";
	
	//room_reservations table
	private static final String ROOM_RES_TABLE = "room_reservations";
	private static final String ROOM_RES_ID = "room_reservations.id";
	private static final String ROOM_ID_FK = "room_reservations.room_id";
	private static final String RES_ID_FK = "room_reservations.reservation_id";
	
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
	public Customer addCustomer(String firstName, String lastName, String email, String phone, String password) {
	    //final String INSERT_CUST = "INSERT INTO "+CUSTOMER_TABLE+" ("+FIRST_NAME+", "+LAST_NAME+", "+EMAIL+
	      //      ", "+PASSWORD+", "+PHONE+")"+ " VALUES(?, ?, ?, ?, ?)";
	    final String INSERT_CUST = String.format(" INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
	            CUSTOMER_TABLE, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE);
	    
	    boolean insertSucceeded = false;
	    Customer cust = null;
	    long id;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(INSERT_CUST, Statement.RETURN_GENERATED_KEYS)) {
	        pStmt.setString(1, firstName);
	        pStmt.setString(2, lastName);
	        pStmt.setString(3, email);
	        pStmt.setString(4, password);
	        pStmt.setString(5, phone);
	        
	        insertSucceeded = pStmt.executeUpdate() == 1;
	        
	        ResultSet rs = pStmt.getGeneratedKeys();
	        
	        if (insertSucceeded && rs.first()) {
	            id = rs.getLong(1);
	            cust = new Customer(id, firstName, lastName, email, phone);
	        }
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
		return cust;
	}

	@Override
	public Customer fetchCustomer(String email) {
	    final String GET_CUST = String.format("SELECT %s, %s, %s, %s FROM %s where email= ?", 
	            CUST_ID, FIRST_NAME, LAST_NAME, PHONE, CUSTOMER_TABLE);
	    
	    Customer cust = null;
	    
		try (PreparedStatement pStmt = conn.prepareStatement(GET_CUST)) {
		    pStmt.setString(1, email);
		    ResultSet rs = pStmt.executeQuery();
		    if (rs.first() ) {
		        cust = new Customer(rs.getLong(CUST_ID), rs.getString(FIRST_NAME), rs.getString(LAST_NAME), 
		                email, rs.getString(PHONE));
		    }
		    
		} catch (SQLException sqle) {
		    System.err.println(sqle.getMessage());
		}
		return cust;
	}
	
	@Override
	public boolean updateCustomer(Customer cust) { 
	    
	    final String UPDATE_CUST = "UPDATE "+CUSTOMER_TABLE+" SET "+FIRST_NAME+"= ?, "+
	            LAST_NAME+"= ?, "+EMAIL+"= ?, "+PHONE+"= ?  WHERE "+EMAIL+"= ?";
	    
	    boolean updateSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(UPDATE_CUST)) {
	        
	        pStmt.setString(1, cust.getFirstName());
	        pStmt.setString(2, cust.getLastName());
	        pStmt.setString(3, cust.getEmail());
	        pStmt.setString(4, cust.getPhone());
	        pStmt.setString(5, cust.getEmail());
	        
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
	public Reservation saveReservation(long custId, LocalDate start, LocalDate end) {
	    final String INSERT_RES = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", 
	            RESERVATION_TABLE, CUST_ID_FK, START, END);
	    
	    try(PreparedStatement pStmt = conn.prepareStatement(INSERT_RES)) {
	        conn.setAutoCommit(false);
	        pStmt.setLong(1, custId);
	        pStmt.setDate(2, java.sql.Date.valueOf(start));
	        pStmt.setDate(3, java.sql.Date.valueOf(end));
	        
	        System.out.println(pStmt);
	    } catch (SQLException e) {
            e.printStackTrace();
        }
	    
		throw new UnsupportedOperationException("Not implemented"); 
	}

	@Override
	public Reservation fetchReservation(int id) {
	    throw new UnsupportedOperationException("Not implemented"); 
	}
	
	@Override
    public List<Reservation> fetchReservations(Customer customer) {
	    final String GET_CUST_RES = "SELECT ";
	    
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
    public List<Room> getUnreservedRooms(LocalDate start, LocalDate end) {
        List<Room> rooms = new ArrayList<>();
        
        final String UNRES_ROOMS = "SELECT "+ROOM_PRICE+", "+MAX_GUESTS+", "+ROOM_ID+" FROM "+ROOM_TABLE
                + " LEFT JOIN "+ROOM_RES_TABLE+" ON "+ROOM_ID+" = "+ROOM_ID_FK
                + " LEFT JOIN "+RESERVATION_TABLE+" ON "+RES_ID_FK+" = "+RES_ID
                + " WHERE "+END+" < ? OR "+START+" > ?"
                + " OR "+START+" IS NULL";
        
        try (PreparedStatement pStmt = conn.prepareStatement(UNRES_ROOMS)) {
            pStmt.setDate(1, java.sql.Date.valueOf(start));
            pStmt.setDate(2, java.sql.Date.valueOf(end));
            
            ResultSet rs = pStmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new Room(rs.getInt(ROOM_ID), rs.getBigDecimal(ROOM_PRICE), rs.getInt(MAX_GUESTS)));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }
    
}
