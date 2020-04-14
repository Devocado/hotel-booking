package persistence;

import java.time.LocalDate;
import java.util.List;

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
	
	
	boolean saveReservation(Reservation reservation);
	Reservation fetchReservation(int id);
	List<Reservation> fetchReservations(Customer customer);
	boolean deleteReservation(Reservation reservation);
	boolean updateReservation(Reservation oldReservation, Reservation newReservation);
	
	Room getRoom(int roomNumber);
	List<Room> getRooms();
	List<Room> getUnreservedRooms(LocalDate start, LocalDate end);

}
