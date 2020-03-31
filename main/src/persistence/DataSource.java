package persistence;

import java.util.List;

import types.Customer;
import types.Reservation;

public interface DataSource {
	
	boolean addCustomer(Customer customer, String password);
	Customer fetchCustomer(String email);
	boolean deleteCustomer(Customer customer);
	boolean updateCustomer(Customer oldCust, Customer newCust);
	String fetchPassword(String email);
	boolean updateCustomerPassword(Customer cust, String newPassword);
	
	
	boolean saveReservation(Reservation reservation);
	Reservation fetchReservation(int id);
	List<Reservation> fetchReservations(Customer customer);
	boolean deleteReservation(Reservation reservation);
	boolean updateReservation(Reservation oldReservation, Reservation newReservation);

}
