package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.DataSource;
import types.Customer;
import types.Reservation;
import types.Room;

public class DataBase implements DataSource {
	
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/hotel";
	
	//customers table
	private static final String CUSTOMER_TABLE = "customers";
	private static final String CUST_ID = "customers.id";
	private static final String FIRST_NAME = "customers.f_name";
	private static final String LAST_NAME = "customers.l_name";
	private static final String EMAIL = "customers.email";
	private static final String PASSWORD = "customers.password";
	private static final String PHONE = "customers.phone";
	
	//reservations table
	private static final String RES_TABLE = "reservations";
	private static final String RES_ID = "reservations.id";
	private static final String CUST_ID_FK = "reservations.customer_id";
	
	//rooms table
	private static final String ROOM_TABLE = "rooms";
	private static final String ROOM_ID = "rooms.id";
	private static final String ROOM_PRICE = "rooms.night_price";
	private static final String MAX_GUESTS = "rooms.max_guests";
	
	//room_reservations table
	private static final String ROOM_RES_TABLE = "room_reservations";
	private static final String ROOM_RES_ID = "room_reservations.id";
	private static final String ROOM_ID_FK = "room_reservations.room_id";
	private static final String RES_ID_FK = "room_reservations.reservation_id";
	private static final String START = "room_reservations.start_date";
    private static final String END = "room_reservations.end_date";
	
	private Connection conn;
	
	public DataBase() {
		try {
			conn = DriverManager.getConnection(DATABASE_URL, "hotelapp", "password");
		} catch (SQLException sqle) {
			throw new RuntimeException("Could not connect to database");
		}
	}

	@Override
	public Customer addCustomer(String firstName, String lastName, String email, String phone, String password) {
	    
	    final String INSERT_CUST = " INSERT INTO "+CUSTOMER_TABLE
	            + " ("+FIRST_NAME+", "+LAST_NAME+", "+EMAIL+", "+PASSWORD+", "+PHONE+") VALUES (?, ?, ?, ?, ?)";
	    
	    Customer cust = null;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(INSERT_CUST, Statement.RETURN_GENERATED_KEYS)) {
	        pStmt.setString(1, firstName);
	        pStmt.setString(2, lastName);
	        pStmt.setString(3, email);
	        pStmt.setString(4, password);
	        pStmt.setString(5, phone);
	        
	        boolean insertSucceeded = pStmt.executeUpdate() == 1;
	        ResultSet rs = pStmt.getGeneratedKeys();
	        
	        if (insertSucceeded && rs.first()) {
	            cust = new Customer(rs.getLong(1), firstName, lastName, email, phone);
	        }
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
		return cust;
	}

	@Override
	public Customer fetchCustomer(String email) {
	    final String GET_CUST = 
	            "SELECT "+CUST_ID+", "+FIRST_NAME+", "+LAST_NAME+", "+PHONE+" FROM "+CUSTOMER_TABLE+" where email= ?";
	    
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
	    
	    final String UPDATE_CUST = "UPDATE "+CUSTOMER_TABLE+" "
	            + "SET "+FIRST_NAME+"= ?, "+LAST_NAME+"= ?, "+EMAIL+"= ?, "+PHONE+"= ?  WHERE "+CUST_ID+"= ?";
	    
	    boolean updateSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(UPDATE_CUST)) {
	        
	        pStmt.setString(1, cust.getFirstName());
	        pStmt.setString(2, cust.getLastName());
	        pStmt.setString(3, cust.getEmail());
	        pStmt.setString(4, cust.getPhone());
	        pStmt.setLong(5, cust.getId());
	        
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
	public boolean deleteCustomer(Customer cust) {
	    
	    final String DELETE_CUST = "DELETE FROM "+CUSTOMER_TABLE+" WHERE "+CUST_ID+"= ?";
	    
	    boolean deleteSucceeded = false;
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(DELETE_CUST)) {
	        pStmt.setLong(1, cust.getId());
	        
	        int rowsAffected = pStmt.executeUpdate();
	        
	        assert rowsAffected <= 1: "Panic Stations!! More than one customer was deleted!!";
	        
	        deleteSucceeded = rowsAffected == 1;
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle.getMessage());
	    }
	    return deleteSucceeded;
	}
	
	@Override
	public String fetchPassword(Customer cust) {
	    final String GET_PASS = "SELECT "+PASSWORD+" FROM "+CUSTOMER_TABLE+" WHERE "+EMAIL+"= ?";
	    
	    String password = "";
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(GET_PASS)) {
	        
	        pStmt.setString(1, cust.getEmail());
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
	public Reservation saveReservation(Customer cust, LocalDate start, LocalDate end, List<Room> rooms) {

	    final String INSERT_RES = "INSERT INTO "+RES_TABLE+" ("+CUST_ID_FK+") VALUES (?)";
	    
	    final String INSERT_ROOM_RES = "INSERT INTO "+ROOM_RES_TABLE+" ("+ROOM_ID_FK+", "+RES_ID_FK+", "
	    + START+", "+END+") values (?, ?, ?, ?)";
	 
	    
	    try(PreparedStatement insrtRes = conn.prepareStatement(INSERT_RES, Statement.RETURN_GENERATED_KEYS); 
	                PreparedStatement insrtRoomRes = conn.prepareStatement(INSERT_ROOM_RES)) {
	        conn.setAutoCommit(false);
	        
	        insrtRes.setLong(1, cust.getId());
	        insrtRes.executeUpdate();
	        
	        ResultSet rs = insrtRes.getGeneratedKeys();
            
            rs.next();
            long res_id = rs.getLong(1);
            
	        for(Room room : rooms) {
	            insrtRoomRes.setLong(1, room.getRoomNumber());
	            insrtRoomRes.setLong(2, res_id);
	            insrtRoomRes.setDate(3, java.sql.Date.valueOf(start));
	            insrtRoomRes.setDate(4, java.sql.Date.valueOf(end));
	            insrtRoomRes.executeUpdate();
	        }
	        
	        conn.commit();
	        
	        return new Reservation(res_id, cust, start, end, rooms);
	        
	    } catch (SQLException e) {
	        try {
	            System.out.println("Rolling back!");
	            conn.rollback();
	        } catch (SQLException sqle) {
	            sqle.printStackTrace();
	        }
            e.printStackTrace();
            
            return null;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}

	@Override
	public Reservation fetchReservation(int id) {
	    throw new UnsupportedOperationException("Not implemented"); 
	}
	
	@Override
    public List<Reservation> fetchReservations(Customer customer) {
	    final String GET_CUST_RES = "SELECT DISTINCT "+RES_ID+", "+START+", "+END+" FROM "+RES_TABLE 
	            + " RIGHT JOIN "+ROOM_RES_TABLE+" ON "+RES_ID+" = "+ RES_ID_FK
	            + " where "+CUST_ID_FK+" = ?;";
	    
	    List<Reservation> reservations = new ArrayList<>();
	    
	    try (PreparedStatement pStmt = conn.prepareStatement(GET_CUST_RES)) {
	        
	        pStmt.setLong(1, customer.getId());
	        
	        ResultSet rs = pStmt.executeQuery();
	        
	        while (rs.next()) {
	            
	            long resId = rs.getLong(RES_ID);
	            LocalDate startDate = rs.getDate(START).toLocalDate();
	            LocalDate endDate = rs.getDate(END).toLocalDate();
	            List<Room> rooms = getResRooms(rs.getLong(RES_ID));
	            
	            reservations.add(new Reservation(resId, customer, startDate, endDate, rooms));
	        }
	        
	    } catch (SQLException sqle) {
	        System.err.println(sqle);
	    }
	    
	    return reservations;
	}
	
	public List<Room> getResRooms(long resId) {
        final String GET_ROOMS = "SELECT "+ROOM_ID+", "+MAX_GUESTS+", "+ROOM_PRICE+" from "+ROOM_RES_TABLE
                + " inner join "+ROOM_TABLE+" on "+ROOM_ID_FK+" = "+ROOM_ID+" where "+RES_ID_FK+" = ?;";
        
        List<Room> rooms = new ArrayList<>();
        
        try (PreparedStatement pStmt = conn.prepareStatement(GET_ROOMS)) {
            pStmt.setLong(1, resId);
            
            ResultSet rs = pStmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new Room(rs.getLong(ROOM_ID), rs.getBigDecimal(ROOM_PRICE), rs.getInt(MAX_GUESTS)));
            } 
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return rooms;
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
    public Room getRoom(int roomId) {
        final String GET_ROOM = "SELECT "+ROOM_PRICE+", "+MAX_GUESTS+" FROM "+ROOM_TABLE+" WHERE "+ROOM_ID+"= ?";
        
        Room room = null;
        
        try (PreparedStatement pStmt = conn.prepareStatement(GET_ROOM)) {
            pStmt.setInt(1, roomId);
            ResultSet rs = pStmt.executeQuery();
            
            if (rs.first()) {
                room = new Room(roomId, rs.getBigDecimal(ROOM_PRICE), rs.getInt(MAX_GUESTS));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return room;
    }
    
    @Override
    public Map<Long, Room> getAllRooms() {
        
        final String GET_ROOMS = "SELECT * FROM "+ROOM_TABLE;
        
        Map<Long, Room> rooms = new HashMap<>();
        
        try (PreparedStatement pStmt = conn.prepareStatement(GET_ROOMS)){
            ResultSet rs = pStmt.executeQuery();
            
            long roomId = rs.getLong(ROOM_ID);
            BigDecimal pricePerNight = rs.getBigDecimal(ROOM_PRICE);
            int maxGuests = rs.getInt(MAX_GUESTS);
            
            while(rs.next()) {
                rooms.put(roomId, new Room(roomId, pricePerNight, maxGuests));
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
        return rooms;
    }
    
    @Override
    public Map<Long, Room> getUnreservedRooms(LocalDate startDate, LocalDate endDate) {
        Map<Long, Room> rooms = new HashMap<>();
        
        final String UNRES_ROOMS = "SELECT "+ROOM_ID+", "+ROOM_PRICE+", "+MAX_GUESTS+" FROM "+ROOM_TABLE
                +" WHERE "+ROOM_ID+" NOT IN( "
                        + "SELECT "+ROOM_ID+" FROM "+ROOM_TABLE
                        + " INNER JOIN "+ROOM_RES_TABLE+" ON "+ROOM_ID+" = "+ ROOM_ID_FK
                        + " WHERE "+START+" BETWEEN ? AND ? OR "+END+" BETWEEN ? AND ? "
                        + "OR ? BETWEEN "+START+" AND "+END+")";
        
        try (PreparedStatement pStmt = conn.prepareStatement(UNRES_ROOMS)) {
            
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);
            
            pStmt.setDate(1, start);
            pStmt.setDate(2, end);
            pStmt.setDate(3, start);
            pStmt.setDate(4, end);
            pStmt.setDate(5, start);
            
            System.out.println(pStmt);
            
            ResultSet rs = pStmt.executeQuery();
            
            
            
            while (rs.next()) {
                long roomId = rs.getLong(ROOM_ID);
                BigDecimal pricePerNight = rs.getBigDecimal(ROOM_PRICE);
                int maxGuests = rs.getInt(MAX_GUESTS);
                
                rooms.put(roomId, new Room(roomId, pricePerNight, maxGuests));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }
    
}
