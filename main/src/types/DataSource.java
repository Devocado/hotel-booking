package types;

public interface DataSource {
	
	boolean saveCustomer(Customer customer);
	Customer fetchCustomer();

}
