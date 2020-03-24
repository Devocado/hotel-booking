package types;

public interface DataSource {
	
	boolean saveCustomer(Customer customer);
	Customer fetchCustomer(String email);
	
	boolean saveReservation(Reservation reservation);
	Reservation fetchReservation(int id);

}
