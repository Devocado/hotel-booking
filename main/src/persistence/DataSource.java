package persistence;

import java.util.List;

import types.Customer;
import types.Reservation;

public interface DataSource {
	
	boolean saveCustomer(Customer customer);
	Customer fetchCustomer(String email);
	boolean deleteCustomer(Customer customer);
	boolean updateCustomer(Customer oldCust, Customer newCust);
	
	
	boolean saveReservation(Reservation reservation);
	Reservation fetchReservation(int id);
	List<Reservation> fetchReservations(Customer customer);
	boolean deleteReservation(Reservation reservation);
	boolean updateReservation(Reservation oldReservation, Reservation newReservation);

}
