package persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import types.Customer;
import types.Reservation;
import types.Room;

public interface DataSource {
	
	Customer addCustomer(String firstName, String lastName, String email, String phone, String password);
	Customer fetchCustomer(String email);
	boolean deleteCustomer(Customer customer);
	boolean updateCustomer(Customer cust);
	String fetchPassword(String email);
	boolean updateCustomerPassword(Customer cust, String newPassword);
	
	
	Reservation saveReservation(Customer cust, LocalDate start, LocalDate end, List<Room> rooms);
	Reservation fetchReservation(int id);
	List<Reservation> fetchReservations(Customer customer);
	boolean deleteReservation(Reservation reservation);
	boolean updateReservation(Reservation oldReservation, Reservation newReservation);
	
	Room getRoom(int roomNumber);
	Map<Integer, Room> getRooms();
	Map<Integer, Room> getUnreservedRooms(LocalDate start, LocalDate end);

}