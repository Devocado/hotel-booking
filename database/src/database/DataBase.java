package database;

import types.Customer;
import types.DataSource;

public class DataBase implements DataSource {

	@Override
	public boolean saveCustomer(Customer customer) {
		System.out.println("Saving customer");
		return false;
	}

	@Override
	public Customer fetchCustomer() {
		System.out.println("Fetching customer");
		return null;
	}

}
