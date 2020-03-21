package main;

import java.util.ServiceLoader;

import types.Customer;
import types.DataSource;

public class Main {
	
	private static DataSource dataSource;

	public static void main(String[] args) {
		
		System.out.println("Project is running!");
		dataSource = loadDataSource();
		
		Customer cust = new Customer("Joe", "Bloggs", "joe@bloggs.com");
		dataSource.saveCustomer(cust);

	}
	
	public static DataSource loadDataSource() {
		return ServiceLoader.load(DataSource.class).findFirst().orElseThrow();
	}
}
